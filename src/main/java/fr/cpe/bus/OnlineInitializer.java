package fr.cpe.bus;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.cpe.AppModule;
import fr.cpe.observers.ConnectionServiceMessageObserver;
import fr.cpe.observers.PartieServiceMessageObserver;
import fr.cpe.service.Partie;

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

    private Partie Partie;
    private MethodCallHandler handler;
    private boolean connected = false;

    @Inject
    public OnlineInitializer(Partie partie) {
        this.Partie = partie;
    }

    /**
     * Starts the online infrastructure:
     * <ol>
     *   <li>Creates a MethodCallHandler to listen for remote calls</li>
     *   <li>Registers the local Partie mediator implementation</li>
     *   <li>Adds an observer that converts incoming JSON into local mediator calls</li>
     * </ol>
     */
    public boolean start() {
        try {
            String connectionString = AppModule.getConnectionString();
            String instanceName = System.getenv().getOrDefault("INSTANCE_NAME", "instance-local");

            LOGGER.info("Starting online infrastructure for instance: " + instanceName);

            // Create and start the handler
            handler = new MethodCallHandler(connectionString, HUB);
            handler.register(Partie.class, Partie);
            handler.addObserver(new ConnectionServiceMessageObserver());
            handler.addObserver(new PartieServiceMessageObserver());
            handler.start();

            connected = true;

            LOGGER.info("Online infrastructure started successfully");
            return true;
        } catch (Exception e) {
            connected = false;
            LOGGER.log(Level.WARNING, "Failed to start online infrastructure", e);
            return false;
        }
    }

    public boolean isConnected() {
        return connected;
    }

    /**
     * Stops the online infrastructure and cleans up resources.
     */
    public void stop() {
        if (handler != null) {
            handler.stop();
            LOGGER.info("Online infrastructure stopped");
        }
        connected = false;
    }
}
