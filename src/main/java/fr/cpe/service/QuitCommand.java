package fr.cpe.service;

/**
 * Command for quitting the game.
 */
public class QuitCommand implements Command {

    private MessageStore messageStore;

    public QuitCommand(MessageStore messageStore) {
        this.messageStore = messageStore;
    }

    @Override
    public void execute() {}
}