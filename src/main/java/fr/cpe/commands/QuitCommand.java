package fr.cpe.commands;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.cpe.model.Dresseur;
import fr.cpe.service.MessageStore;
import fr.cpe.service.Partie;

/**
 * Command for quitting the game.
 */
public class QuitCommand implements Command {

    private final MessageStore messageStore;
    private final Dresseur dresseur;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public QuitCommand(MessageStore messageStore, Dresseur dresseur) {
        this.messageStore = messageStore;
        this.dresseur = dresseur;
    }

    @Override
    public void execute() {
        try {
            String dresseurJson = objectMapper.writeValueAsString(dresseur);
            String json = "{\"interface\":\"" + Partie.class.getName() + "\"," +
                         "\"method\":\"handleQuit\"," +
                         "\"args\":[" + dresseurJson + "]}";
            this.messageStore.setLastMessage(json);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing quit data to JSON", e);
        }
    }
}