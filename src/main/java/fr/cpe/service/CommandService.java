package fr.cpe.service;

import java.util.logging.Logger;

public abstract class CommandService {

    protected final CommandExecutor commandExecutor;
    protected final MessageStore messageStore;
    protected static final Logger LOGGER = Logger.getLogger(CommandService.class.getName());

    public CommandService(CommandExecutor commandExecutor, MessageStore messageStore) {
        this.commandExecutor = commandExecutor;
        this.messageStore = messageStore;
    }

}
