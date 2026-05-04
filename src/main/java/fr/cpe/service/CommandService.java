package fr.cpe.service;

import java.util.logging.Logger;
import java.util.logging.Level;

import com.azure.messaging.webpubsub.WebPubSubServiceClient;
import com.azure.messaging.webpubsub.models.WebPubSubContentType;
import com.google.inject.Inject;
import fr.cpe.bus.MethodCallHandler;
import fr.cpe.commands.Command;

public abstract class CommandService {

    protected final CommandExecutor commandExecutor;
    protected final MessageStore messageStore;
    protected final WebPubSubServiceClient publisher;
    protected static final Logger LOGGER = Logger.getLogger(CommandService.class.getName());

    @Inject
    public CommandService(CommandExecutor commandExecutor, MessageStore messageStore, WebPubSubServiceClient publisher) {
        this.commandExecutor = commandExecutor;
        this.messageStore = messageStore;
        this.publisher = publisher;
    }

    public MessageStore getMessageStore() {
        return this.messageStore;
    }

    protected void executeCommand(Command command) {
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

            // Process the message locally first so observers can react to self-originated commands.
            MethodCallHandler.notifyLocalObservers(json);

            // Send the JSON to the bus
            try {
                publisher.sendToAll(json, WebPubSubContentType.APPLICATION_JSON);
                LOGGER.info("Command executed and sent to bus: " + command.getClass().getSimpleName());
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Failed to send command to bus", e);
            }
        });
        newThread.start();
    }

}
