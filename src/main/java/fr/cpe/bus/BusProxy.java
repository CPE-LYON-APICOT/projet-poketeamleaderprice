package fr.cpe.bus;

import com.azure.messaging.webpubsub.WebPubSubServiceClient;
import com.azure.messaging.webpubsub.models.WebPubSubContentType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Dynamic proxy that intercepts method calls and sends them over Azure Web PubSub.
 * <p>
 * This proxy serializes method invocations to JSON and publishes them to all
 * subscribers via the Web PubSub hub. The actual method execution happens on
 * remote instances that listen to the bus.
 */
public class BusProxy implements InvocationHandler {

    private static final Logger LOGGER = Logger.getLogger(BusProxy.class.getName());
    private static final String DEFAULT_HUB = "game";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final Class<?> iface;
    private final WebPubSubServiceClient publisher;
    private final String hub;

    /**
     * Creates a new BusProxy for the given interface.
     *
     * @param iface     the interface to proxy
     * @param publisher the Web PubSub client for publishing messages
     * @param hub       the hub name to publish to
     */
    public BusProxy(Class<?> iface, WebPubSubServiceClient publisher, String hub) {
        this.iface = iface;
        this.publisher = publisher;
        this.hub = hub;
    }

    /**
     * Factory method to create a proxy instance for the given interface.
     *
     * @param iface     the interface to proxy
     * @param publisher the Web PubSub client for publishing messages
     * @param <T>       the interface type
     * @return a proxy instance implementing the interface
     */
    @SuppressWarnings("unchecked")
    public static <T> T create(Class<T> iface, WebPubSubServiceClient publisher) {
        return (T) Proxy.newProxyInstance(
                iface.getClassLoader(),
                new Class<?>[]{iface},
                new BusProxy(iface, publisher, DEFAULT_HUB)
        );
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Let Object methods pass through without sending to the bus
        if (method.getDeclaringClass() == Object.class) {
            return handleObjectMethod(method, args);
        }

        // Build the JSON message
        Map<String, Object> message = new HashMap<>();
        message.put("interface", iface.getName());
        message.put("method", method.getName());
        message.put("paramTypes", Arrays.stream(method.getParameterTypes())
                .map(Class::getName)
                .toArray(String[]::new));
        message.put("args", args != null ? args : new Object[0]);

        String json = OBJECT_MAPPER.writeValueAsString(message);

        // Send to all subscribers with JSON content type
        try {
            publisher.sendToAll(json, WebPubSubContentType.APPLICATION_JSON);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to send message to Web PubSub: " + iface.getName() + "." + method.getName(), e);
        }

        // Return null for void methods, or default values for primitives
        return getDefaultReturnValue(method.getReturnType());
    }

    private Object handleObjectMethod(Method method, Object[] args) {
        String name = method.getName();
        switch (name) {
            case "hashCode":
                return System.identityHashCode(this);
            case "equals":
                return args != null && args.length > 0 && args[0] == this;
            case "toString":
                return "BusProxy[" + iface.getName() + "]";
            default:
                return null;
        }
    }

    private Object getDefaultReturnValue(Class<?> returnType) {
        if (returnType == void.class || returnType == Void.class) {
            return null;
        }
        if (returnType.isPrimitive()) {
            if (returnType == boolean.class) return false;
            if (returnType == byte.class) return (byte) 0;
            if (returnType == char.class) return '\0';
            if (returnType == short.class) return (short) 0;
            if (returnType == int.class) return 0;
            if (returnType == long.class) return 0L;
            if (returnType == float.class) return 0.0f;
            if (returnType == double.class) return 0.0;
        }
        return null;
    }
}
