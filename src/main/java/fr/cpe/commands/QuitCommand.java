package fr.cpe.commands;

import fr.cpe.service.MessageStore;

/**
 * Command for quitting the game.
 */
public class QuitCommand implements Command {

    private final MessageStore messageStore;

    public QuitCommand(MessageStore messageStore) {
        this.messageStore = messageStore;
    }

    @Override
    public void execute() {
        this.messageStore.setLastMessage("{\"commandType\":\"quit\"}");
    }
}