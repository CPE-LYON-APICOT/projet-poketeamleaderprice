package fr.cpe.service;

/**
 * Service interface for distributed hello messaging via Azure Web PubSub.
 */
public interface HelloService {
    void sayHello(String message);
}
