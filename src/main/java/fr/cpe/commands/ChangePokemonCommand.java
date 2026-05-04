package fr.cpe.commands;

import fr.cpe.service.MessageStore;

/**
 * Command for changing Pokémon.
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