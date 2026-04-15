package fr.cpe.bus.commands;

import fr.cpe.service.MessageStore;

/**
 * Command for using an item.
 */
public class UseItemCommand implements Command {

    private MessageStore messageStore;

    public UseItemCommand(MessageStore messageStore) {
        this.messageStore = messageStore;
    }

    @Override
    public void execute() {
        this.messageStore.setLastMessage("{\"commandType\":\"useItem\"}");
    }
}