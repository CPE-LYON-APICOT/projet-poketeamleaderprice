package fr.cpe.bus;

/**
 * Observer interface for messages received from the bus.
 */
public interface MessageObserver {

    /**
     * Called when a raw JSON message arrives.
     *
     * @param json the received JSON payload
     * @return true if the observer handled the message, false otherwise
     */
    boolean onMessage(String json);
}
