package fr.cpe.commands;

import fr.cpe.model.Attaque;
import fr.cpe.model.Dresseur;
import fr.cpe.model.Pokemon;
import fr.cpe.service.MessageStore;

/**
 * Command for executing an attack action.
 */
public class AttackCommand implements Command {

    private final MessageStore messageStore;
    private final Dresseur dresseurAttaquant;
    private final Attaque attaque;

    public AttackCommand(MessageStore messageStore, Dresseur dresseurAttaquant, Attaque attaque) {
        this.messageStore = messageStore;
        this.dresseurAttaquant = dresseurAttaquant;
        this.attaque = attaque;
    }

    @Override
    public void execute() {
        String json = "{\"commandType\":\"attack\",\"dresseurID\":\"" + dresseurAttaquant.getIndex() + "\",\"attackID\":\"" + attaque.getId() + "\"}";
        this.messageStore.setLastMessage(json);
    }
}