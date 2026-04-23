package fr.cpe.service;

import java.util.logging.Logger;

import fr.cpe.commands.Command;

public abstract class CommandService {

    protected final CommandExecutor commandExecutor;
    protected final MessageStore messageStore;
    protected static final Logger LOGGER = Logger.getLogger(CommandService.class.getName());

    public CommandService(CommandExecutor commandExecutor, MessageStore messageStore) {
        this.commandExecutor = commandExecutor;
        this.messageStore = messageStore;
    }

    public MessageStore getMessageStore() {
        return this.messageStore;
    }

    public void executeCommand(Command command) {
        System.out.println("FZJZJOFZJOFZOIJFZOFZIJOIJF");
        Thread newThread = new Thread(() -> {
            if (command == null) {
                return;
            }

            this.commandExecutor.execute(command);
            String json = this.messageStore.getLastMessage();

            if (json == null || json.isBlank()) {
                LOGGER.warning("Command did not produce a JSON message: " + command.getClass().getSimpleName());
                return;
            }
            LOGGER.info("Command executed successfully: " + command.getClass().getSimpleName());
        });
        newThread.start();
    }

}
