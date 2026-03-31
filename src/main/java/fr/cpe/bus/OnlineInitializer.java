package fr.cpe.bus;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.cpe.AppModule;
import fr.cpe.service.HelloService;
import fr.cpe.service.HelloServiceImpl;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Initializes the online bus infrastructure.
 * <p>
 * This class sets up the MethodCallHandler to listen for remote method calls
 * and demonstrates the distributed method bus by sending a test message.
 * <p>
 * Call {@link #start()} to begin listening for messages and {@link #stop()}
 * to clean up when the application shuts down.
 */
@Singleton
public class OnlineInitializer {

    private static final Logger LOGGER = Logger.getLogger(OnlineInitializer.class.getName());
    private static final String HUB = "game";

    private final HelloService helloService;
    private MethodCallHandler handler;

    @Inject
    public OnlineInitializer(HelloService helloService) {
        this.helloService = helloService;
    }

    /**
     * Starts the online infrastructure:
     * <ol>
     *   <li>Creates a MethodCallHandler to listen for remote calls</li>
     *   <li>Registers the HelloServiceImpl implementation</li>
     *   <li>Sends a test message to demonstrate the bus</li>
     * </ol>
     */
    public void start() {
        try {
            String connectionString = AppModule.getConnectionString();
            String instanceName = System.getenv().getOrDefault("INSTANCE_NAME", "instance-local");

            LOGGER.info("Starting online infrastructure for instance: " + instanceName);

            // Create and start the handler
            handler = new MethodCallHandler(connectionString, HUB);
            handler.register(HelloService.class, new HelloServiceImpl());
            handler.start();

            // Send a test message via the proxy
            helloService.sayHello("Hello depuis JavaFX !");

            LOGGER.info("Online infrastructure started successfully");
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to start online infrastructure", e);
        }
    }

    /**
     * Stops the online infrastructure and cleans up resources.
     */
    public void stop() {
        if (handler != null) {
            handler.stop();
            LOGGER.info("Online infrastructure stopped");
        }
    }
}
