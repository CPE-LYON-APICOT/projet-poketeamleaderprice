package fr.cpe.commands;

import fr.cpe.service.MessageStore;

public class HostGameCommand implements Command {

    private final String username;
    private MessageStore messageStore;

    public HostGameCommand(MessageStore messageStore, String username) {
        this.messageStore = messageStore;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void execute() {
        String json = "{\"type\":\"hostgame\",\"username\":\"" + username + "\"}";
        this.messageStore.setLastMessage(json);
    }
}
