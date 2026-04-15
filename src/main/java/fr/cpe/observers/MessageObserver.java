package fr.cpe.observers;

import java.util.logging.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Observer interface for messages received from the bus.
 */
public abstract class MessageObserver {

    protected static final Logger LOGGER = Logger.getLogger(PartieServiceMessageObserver.class.getName());
    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Called when a raw JSON message arrives.
     *
     * @param json the received JSON payload
     * @return true if the observer handled the message, false otherwise
     */
    public abstract boolean onMessage(String json);
}
