package fr.cpe.service;

/**
 * Command for executing an attack action.
 */
public class AttackCommand implements Command {

    private MessageStore messageStore;

    public AttackCommand(MessageStore messageStore) {
        this.messageStore = messageStore;
    }

    @Override
    public void execute() {
    }
}