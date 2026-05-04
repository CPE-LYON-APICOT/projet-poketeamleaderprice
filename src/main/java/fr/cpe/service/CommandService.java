package fr.cpe.service;

import java.util.logging.Logger;
import java.util.logging.Level;

import com.azure.messaging.webpubsub.WebPubSubServiceClient;
import com.azure.messaging.webpubsub.models.WebPubSubContentType;
import com.google.inject.Inject;
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

    public void executeCommand(Command command) {
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

            // Send the JSON to the bus. Add a sender/instance id so receivers can ignore self-originated messages.
            try {
                // Try to annotate message with sender id
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                java.util.Map<String, Object> messageMap = mapper.readValue(json, java.util.Map.class);
                String instanceName = System.getenv().getOrDefault("INSTANCE_NAME", "instance-local");
                messageMap.put("sender", instanceName);
                String annotatedJson = mapper.writeValueAsString(messageMap);

                // Log annotated JSON and then send
                LOGGER.info(() -> "Sending to bus: " + annotatedJson);
                publisher.sendToAll(annotatedJson, WebPubSubContentType.APPLICATION_JSON);
                LOGGER.info("Command executed and sent to bus: " + command.getClass().getSimpleName());
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Failed to send command to bus", e);
            }
        });
        newThread.start();
    }

}
