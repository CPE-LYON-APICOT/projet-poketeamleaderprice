package fr.cpe.service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.google.inject.Singleton;

import fr.cpe.observers.MessageObserver;

/**
 * Simple shared store for the last message received via the bus.
 */
@Singleton
public class MessageStore {

    private volatile String lastMessage = "";
    private final List<MessageObserver> observers = new CopyOnWriteArrayList<>();

    public void setLastMessage(String message) {
        this.lastMessage = message;
        System.out.println("[MessageStore] lastMessage=" + this.lastMessage);
        notifyObservers(message);
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void addObserver(MessageObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(MessageObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(String message) {
        for (MessageObserver observer : observers) {
            try {
                observer.onMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
