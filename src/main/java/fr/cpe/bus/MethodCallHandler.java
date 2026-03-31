package fr.cpe.bus;

import com.azure.messaging.webpubsub.client.WebPubSubClient;
import com.azure.messaging.webpubsub.client.WebPubSubClientBuilder;
import com.azure.messaging.webpubsub.WebPubSubServiceClient;
import com.azure.messaging.webpubsub.WebPubSubServiceClientBuilder;
import com.azure.messaging.webpubsub.models.GetClientAccessTokenOptions;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Listens to Azure Web PubSub via WebSocket and dispatches method calls
 * to registered implementations.
 * <p>
 * This handler receives JSON messages containing serialized method invocations
 * and uses reflection to invoke the corresponding implementation.
 */
public class MethodCallHandler {

    private static final Logger LOGGER = Logger.getLogger(MethodCallHandler.class.getName());
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final String connectionString;
    private final String hub;
    private final Map<String, Object> implementations = new ConcurrentHashMap<>();
    private WebPubSubClient client;

    /**
     * Creates a new MethodCallHandler.
     *
     * @param connectionString the Azure Web PubSub connection string
     * @param hub              the hub name to subscribe to
     */
    public MethodCallHandler(String connectionString, String hub) {
        this.connectionString = connectionString;
        this.hub = hub;
    }

    /**
     * Registers an implementation for the given interface.
     *
     * @param iface the interface class
     * @param impl  the implementation instance
     * @param <T>   the interface type
     */
    public <T> void register(Class<T> iface, T impl) {
        implementations.put(iface.getName(), impl);
    }

    /**
     * Starts listening to Web PubSub messages.
     */
    public void start() {
        try {
            // Create a service client to get the access token
            WebPubSubServiceClient serviceClient = new WebPubSubServiceClientBuilder()
                    .connectionString(connectionString)
                    .hub(hub)
                    .buildClient();

            // Get the client access URL
            GetClientAccessTokenOptions options = new GetClientAccessTokenOptions();
            String clientAccessUrl = serviceClient.getClientAccessToken(options).getUrl();

            // Create the WebSocket client
            client = new WebPubSubClientBuilder()
                    .clientAccessUrl(clientAccessUrl)
                    .buildClient();

            // Subscribe to server messages
            client.addOnServerMessageEventHandler(event -> {
                String json = extractJsonFromData(event.getData());
                if (json != null) {
                    dispatch(json);
                }
            });

            // Subscribe to group messages as well
            client.addOnGroupMessageEventHandler(event -> {
                String json = extractJsonFromData(event.getData());
                if (json != null) {
                    dispatch(json);
                }
            });

            // Start the client
            client.start();

            LOGGER.info("MethodCallHandler started, listening on hub: " + hub);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to start MethodCallHandler", e);
        }
    }

    /**
     * Extracts JSON string from WebPubSub data, handling different data types.
     *
     * @param data the data from the WebPubSub event
     * @return the JSON string, or null if extraction failed
     */
    private String extractJsonFromData(Object data) {
        if (data == null) {
            LOGGER.warning("Received null data from WebPubSub");
            return null;
        }
        if (data instanceof String) {
            return (String) data;
        }
        // For BinaryData or other types, try toString() as fallback
        try {
            return data.toString();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to extract JSON from data", e);
            return null;
        }
    }

    /**
     * Stops listening and closes the WebSocket connection.
     */
    public void stop() {
        if (client != null) {
            try {
                client.stop();
                LOGGER.info("MethodCallHandler stopped");
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error stopping MethodCallHandler", e);
            }
        }
    }

    /**
     * Dispatches a JSON message to the appropriate implementation.
     *
     * @param json the JSON message containing method call information
     */
    @SuppressWarnings("unchecked")
    private void dispatch(String json) {
        try {
            Map<String, Object> message = OBJECT_MAPPER.readValue(json, Map.class);

            String interfaceName = (String) message.get("interface");
            String methodName = (String) message.get("method");
            List<String> paramTypeNames = (List<String>) message.get("paramTypes");
            List<Object> argsList = (List<Object>) message.get("args");

            // Find the implementation
            Object impl = implementations.get(interfaceName);
            if (impl == null) {
                LOGGER.warning("No implementation registered for: " + interfaceName);
                return;
            }

            // Resolve parameter types
            Class<?>[] paramTypes = new Class<?>[paramTypeNames.size()];
            for (int i = 0; i < paramTypeNames.size(); i++) {
                paramTypes[i] = resolveType(paramTypeNames.get(i));
            }

            // Find the method
            Method method = impl.getClass().getMethod(methodName, paramTypes);

            // Deserialize arguments to the correct types
            Object[] args = new Object[argsList.size()];
            for (int i = 0; i < argsList.size(); i++) {
                args[i] = OBJECT_MAPPER.convertValue(argsList.get(i), paramTypes[i]);
            }

            // Invoke the method
            method.invoke(impl, args);

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error dispatching message: " + json, e);
        }
    }

    /**
     * Resolves a type name to a Class object, handling primitives.
     *
     * @param typeName the fully qualified type name
     * @return the Class object
     * @throws ClassNotFoundException if the class cannot be found
     */
    private Class<?> resolveType(String typeName) throws ClassNotFoundException {
        switch (typeName) {
            case "int":
                return int.class;
            case "long":
                return long.class;
            case "double":
                return double.class;
            case "float":
                return float.class;
            case "boolean":
                return boolean.class;
            case "byte":
                return byte.class;
            case "short":
                return short.class;
            case "char":
                return char.class;
            case "void":
                return void.class;
            default:
                return Class.forName(typeName);
        }
    }
}
