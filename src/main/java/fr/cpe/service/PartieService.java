package fr.cpe.service;

/**
 * Service for managing game logic related to Partie.
 * Acts as the receiver for Command objects.
 */
public interface PartieService {

    void handleAttack();

    void handleChangePokemon();

    void handleUseItem();

    void handleQuit();
}