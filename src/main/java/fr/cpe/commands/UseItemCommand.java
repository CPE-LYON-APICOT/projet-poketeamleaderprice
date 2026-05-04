package fr.cpe.commands;

import fr.cpe.service.MessageStore;
import fr.cpe.service.Partie;

/**
 * Command for using an item.
 */
public class UseItemCommand implements Command {

    private final MessageStore messageStore;

    public UseItemCommand(MessageStore messageStore) {
        this.messageStore = messageStore;
    }

    @Override
    public void execute() {
        String json = "{\"interface\":\"" + Partie.class.getName() + "\"," +
                "\"method\":\"handleUseItem\"," +
                "\"args\":[]}";
        this.messageStore.setLastMessage(json);
    }
}
