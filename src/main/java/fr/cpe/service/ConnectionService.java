package fr.cpe.service;

import com.google.inject.Singleton;

import fr.cpe.commands.ConnectCommand;
import fr.cpe.commands.DisconnectCommand;
import fr.cpe.commands.HostGameCommand;

@Singleton
public class ConnectionService extends CommandService {

    public ConnectionService(CommandExecutor commandExecutor, MessageStore messageStore) {
        super(commandExecutor, messageStore);
    }

    public void connect(String username) {
        if (username == null || username.isBlank()) {
            LOGGER.warning("Attempted to connect with an invalid username.");
            return;
        }

        ConnectCommand command = new ConnectCommand(messageStore, username);
        this.commandExecutor.execute(command);
        String json = this.messageStore.getLastMessage();

        if (json == null || json.isBlank()) {
            LOGGER.warning("ConnectCommand did not produce a JSON message for username: " + username);
            return;
        }

        LOGGER.info("User connected successfully: " + username);
    }

    public void disconnect(String username) {
        if (username == null || username.isBlank()) {
            LOGGER.warning("Attempted to disconnect with an invalid username.");
            return;
        }

        DisconnectCommand command = new DisconnectCommand(messageStore, username);
        this.commandExecutor.execute(command);
        String json = this.messageStore.getLastMessage();

        if (json == null || json.isBlank()) {
            LOGGER.warning("DisconnectCommand did not produce a JSON message for username: " + username);
            return;
        }

        LOGGER.info("User disconnected successfully: " + username);
    }

    public void hostGame(String username) {
        if (username == null || username.isBlank()) {
            LOGGER.warning("Attempted to host a game with an invalid username.");
            return;
        }

        HostGameCommand command = new HostGameCommand(messageStore, username);
        this.commandExecutor.execute(command);
        String json = this.messageStore.getLastMessage();

        if (json == null || json.isBlank()) {
            LOGGER.warning("HostGameCommand did not produce a JSON message for username: " + username);
            return;
        }

        LOGGER.info("User hosted a game successfully: " + username);
    }

}
