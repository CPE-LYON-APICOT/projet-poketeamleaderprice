package fr.cpe.service;

/**
 * Command for changing Pokemon.
 */
public class ChangePokemonCommand implements Command {

    private MessageStore messageStore;

    public ChangePokemonCommand(MessageStore messageStore) {
        this.messageStore = messageStore;
    }

    @Override
    public void execute() {}
}