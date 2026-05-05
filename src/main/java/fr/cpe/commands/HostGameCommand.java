package fr.cpe.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cpe.model.Dresseur;
import fr.cpe.model.Stade;
import fr.cpe.service.MessageStore;
import fr.cpe.service.Partie;

public class HostGameCommand implements Command {

    private final Dresseur dresseur;
    private final Stade stade;
    private final MessageStore messageStore;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public HostGameCommand(MessageStore messageStore, Dresseur dresseur, Stade stade) {
        this.messageStore = messageStore;
        this.dresseur = dresseur;
        this.stade = stade;
    }

    public Dresseur getDresseur() {
        return dresseur;
    }

    public Stade getStade() {
        return stade;
    }

    @Override
    public void execute() {
        try {
            String dresseurJson = OBJECT_MAPPER.writeValueAsString(this.dresseur);
            String stadeJson = OBJECT_MAPPER.writeValueAsString(this.stade);
            
            String json = "{\"interface\":\"" + Partie.class.getName() + "\"," +
                         "\"method\":\"hostGame\"," +
                         "\"args\":[" + dresseurJson + "," + stadeJson + "]}";
            
            this.messageStore.setLastMessage(json);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing game data to JSON", e);
        }
    }
}
