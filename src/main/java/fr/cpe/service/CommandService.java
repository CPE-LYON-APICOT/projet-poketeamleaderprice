package fr.cpe.service;

import java.util.logging.Logger;

import com.google.inject.Inject;
import fr.cpe.commands.Command;

public abstract class CommandService {

    protected final CommandExecutor commandExecutor;
    protected final MessageStore messageStore;
    protected static final Logger LOGGER = Logger.getLogger(CommandService.class.getName());

    @Inject
    public CommandService(CommandExecutor commandExecutor, MessageStore messageStore) {
        this.commandExecutor = commandExecutor;
        this.messageStore = messageStore;
    }

    public MessageStore getMessageStore() {
        return this.messageStore;
    }

    public void executeCommand(Command command) {
        if (command == null) {
            return;
        }

        this.commandExecutor.execute(command);
        String json = this.messageStore.getLastMessage();

        if (json == null || json.isBlank()) {
            LOGGER.warning("Command did not produce a message: " + command.getClass().getSimpleName());
            return;
        }

        LOGGER.info("Command executed locally: " + command.getClass().getSimpleName());
    }

}
