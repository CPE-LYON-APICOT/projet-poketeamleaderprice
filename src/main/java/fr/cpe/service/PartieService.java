package fr.cpe.service;

import fr.cpe.model.Attaque;
import fr.cpe.model.Pokemon;

/**
 * Service for managing game logic related to Partie.
 * Acts as the receiver for Command objects.
 */
public interface PartieService {

    void handleAttack(Pokemon pokemonAttaquant, Pokemon pokemonVise, Attaque attaque);

    void handleChangePokemon();

    void handleUseItem();

    void handleQuit();
}