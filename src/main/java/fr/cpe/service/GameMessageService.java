package fr.cpe.service;

/**
 * Service interface for distributed hello messaging via Azure Web PubSub.
 */
public interface GameMessageService {
    void sayHosting();
    void saySearching();
    void sayConnect();
    void sayAttack();
    void sayChangePokemon();
    void sayUseItem();
    void sayQuit();
}
