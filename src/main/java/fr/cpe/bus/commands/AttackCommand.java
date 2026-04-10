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
        this.messageStore.setLastMessage("{commandType: \"attack\" {attaquantID: \""+this.pokemonAttaquant.getNum_Poke()+"\", viseID: \""+this.pokemonVise.getNum_Poke()+"\", attackID: \""+this.attaque.getId()+"\"}}");
    }
}