package fr.cpe.bus.commands;

import fr.cpe.service.MessageStore;

public class ConnectCommand implements Command {

    private final String username;
    private MessageStore messageStore;

    public ConnectCommand(MessageStore messageStore, String username) {
        this.messageStore = messageStore;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void execute() {
        String json = "{\"type\":\"connect\",\"username\":\"" + username + "\"}";
        this.messageStore.setLastMessage(json);
    }

}
