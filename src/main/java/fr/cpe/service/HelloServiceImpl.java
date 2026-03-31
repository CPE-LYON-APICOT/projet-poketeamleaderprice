package fr.cpe.service;

import com.google.inject.Inject;

/**
 * Implementation of HelloService that stores received messages.
 */
public class HelloServiceImpl implements HelloService {

    private final MessageStore messageStore;

    @Inject
    public HelloServiceImpl(MessageStore messageStore) {
        this.messageStore = messageStore;
    }

    @Override
    public void sayHello(String message) {
        messageStore.setLastMessage(message);
        System.out.println("[HelloService] Hello : " + message);
    }
}
