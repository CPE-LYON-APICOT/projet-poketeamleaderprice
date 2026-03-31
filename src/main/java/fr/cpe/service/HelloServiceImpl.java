package fr.cpe.service;

import com.google.inject.Inject;

import fr.cpe.App;

/**
 * Implementation of HelloService that stores received messages.
 */
public class HelloServiceImpl implements HelloService {

    private final MessageStore messageStore;

    @Inject
    public HelloServiceImpl() {
        this.messageStore = App.injector.getInstance(MessageStore.class);
    }

    @Override
    public void sayHello(String message) {
        messageStore.setLastMessage(message);
        System.out.println("[HelloService] Hello : " + message);
    }
}
