package fr.cpe.bus.commands;

import fr.cpe.model.Attaque;
import fr.cpe.model.Pokemon;
import fr.cpe.service.MessageStore;

/**
 * Command for executing an attack action.
 */
public class AttackCommand implements Command {

    private MessageStore messageStore;
    private Pokemon pokemonAttaquant;
    private Pokemon pokemonVise;
    private Attaque attaque;

    public AttackCommand(MessageStore messageStore, Pokemon pokemonAttaquant, Pokemon pokemonVise, Attaque attaque) {
        this.messageStore = messageStore;
        this.pokemonAttaquant = pokemonAttaquant;
        this.pokemonVise = pokemonVise;
        this.attaque = attaque;
    }

    @Override
    public void execute() {
        String json = "{\"commandType\":\"attack\",\"attaquantID\":\"" + pokemonAttaquant.getNum_Poke() + "\",\"viseID\":\"" + pokemonVise.getNum_Poke() + "\",\"attackID\":\"" + attaque.getId() + "\"}";
        this.messageStore.setLastMessage(json);
    }
}