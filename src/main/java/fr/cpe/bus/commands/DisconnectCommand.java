package fr.cpe.bus.commands;

import fr.cpe.service.MessageStore;

public class DisconnectCommand implements Command {

    private final String username;
    private MessageStore messageStore;

    public DisconnectCommand(MessageStore messageStore, String username) {
        this.messageStore = messageStore;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void execute() {
        String json = "{\"type\":\"disconnect\",\"username\":\"" + username + "\"}";
        this.messageStore.setLastMessage(json);
    }

}
