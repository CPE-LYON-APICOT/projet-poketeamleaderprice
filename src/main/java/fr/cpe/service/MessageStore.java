package fr.cpe.service;

import com.google.inject.Singleton;

/**
 * Simple shared store for the last message received via the bus.
 */
@Singleton
public class MessageStore {

    private volatile String lastMessage = "";

    public void setLastMessage(String message) {
        this.lastMessage = message;
    }

    public String getLastMessage() {
        return lastMessage;
    }
}
