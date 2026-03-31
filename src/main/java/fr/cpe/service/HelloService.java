package fr.cpe.service;

/**
 * Service interface for distributed hello messaging via Azure Web PubSub.
 * Mettre toutes les méthodes du game => Tous les joueurs pourront voir les infos (JoueurA attaque B etc.)
 */
public interface HelloService {
    void sayHello(String message);
}
