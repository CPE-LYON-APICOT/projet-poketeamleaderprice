package fr.cpe.commands;

import fr.cpe.model.Dresseur;
import fr.cpe.model.Pokemon;
import fr.cpe.service.MessageStore;

import java.util.Map;

/**
 * Command for changing Pokémon.
 */
public class ChangePokemonCommand implements Command {

    private final MessageStore messageStore;
    private final Dresseur dresseur;
    private final Pokemon nouveauPokemon;

    public ChangePokemonCommand(MessageStore messageStore, Dresseur dresseur, Pokemon nouveauPokemon) {
        this.messageStore = messageStore;
        this.dresseur = dresseur;
        this.nouveauPokemon = nouveauPokemon;
    }

    @Override
    public void execute() {
        int pokemonSlot = -1;
        for (Map.Entry<Integer, Pokemon> entry : this.dresseur.getPokemon().entrySet()) {
            Pokemon pokemon = entry.getValue();
            if (pokemon.getNum_Poke() == this.nouveauPokemon.getNum_Poke()) {
                pokemonSlot = entry.getKey();
            }
        }
        if(pokemonSlot == -1) {
            throw new IllegalArgumentException("Le pokémon choisi n'appartient pas à la liste du dresseur");
        }
        String json = "{\"commandType\":\"changePokemon\"," + "\"dresseurID\":\"" + dresseur.getIndex() + "\"," + "\"pokemonSlot\":\"" + pokemonSlot + "\"}";
        this.messageStore.setLastMessage(json);
    }
}