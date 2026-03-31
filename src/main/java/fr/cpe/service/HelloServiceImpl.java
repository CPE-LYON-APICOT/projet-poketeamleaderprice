package fr.cpe.service;

/**
 * Implementation of HelloService that prints messages to the console.
 */
public class HelloServiceImpl implements HelloService {

    @Override
    public void sayHello(String message) {
        System.out.println("[HelloService] Hello : " + message);
    }
}
