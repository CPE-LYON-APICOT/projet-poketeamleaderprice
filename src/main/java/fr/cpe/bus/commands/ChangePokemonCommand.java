package fr.cpe.bus.commands;

import fr.cpe.service.MessageStore;

/**
 * Command for changing Pokemon.
 */
public class ChangePokemonCommand implements Command {

    private final MessageStore messageStore;

    public ChangePokemonCommand(MessageStore messageStore) {
        this.messageStore = messageStore;
    }

    @Override
    public void execute() {
        this.messageStore.setLastMessage("{\"commandType\":\"changePokemon\"}");
    }
}