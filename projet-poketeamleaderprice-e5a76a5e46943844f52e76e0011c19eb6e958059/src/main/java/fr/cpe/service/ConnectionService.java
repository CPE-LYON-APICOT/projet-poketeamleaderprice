package fr.cpe.service;

import com.google.inject.Singleton;
import com.google.inject.Inject;
import com.azure.messaging.webpubsub.WebPubSubServiceClient;

import fr.cpe.commands.ConnectCommand;
import fr.cpe.commands.DisconnectCommand;
import fr.cpe.commands.HostGameCommand;
import fr.cpe.model.Dresseur;
import fr.cpe.model.Stade;

@Singleton
public class ConnectionService extends CommandService {

    @Inject
    public ConnectionService(CommandExecutor commandExecutor, MessageStore messageStore, WebPubSubServiceClient publisher) {
        super(commandExecutor, messageStore, publisher);
    }

    public void connect(Dresseur dresseur) {
        if (dresseur == null) {
            LOGGER.warning("Attempted to connect with an invalid username.");
            return;
        }
        this.executeCommand(new ConnectCommand(messageStore, dresseur));
    }

    public void disconnect(Dresseur dresseur) {
        if (dresseur == null) {
            LOGGER.warning("Attempted to disconnect with an invalid username.");
            return;
        }
        this.executeCommand(new DisconnectCommand(messageStore, dresseur));
    }

    public void hostGame(Dresseur dresseur, Stade stade) {
        if (dresseur == null) {
            LOGGER.warning("Attempted to host a game with an invalid username.");
            return;
        }
        if (stade == null) {
            LOGGER.warning("Attempted to host a game with an invalid stadium.");
            return;
        }
        this.executeCommand(new HostGameCommand(messageStore, dresseur, stade));
        LOGGER.info("User hosted a game successfully: " + dresseur.getNom());
    }

}
