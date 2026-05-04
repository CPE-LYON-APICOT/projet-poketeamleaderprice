package fr.cpe.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cpe.model.Dresseur;
import fr.cpe.service.MessageStore;
import fr.cpe.service.Partie;

public class ConnectCommand implements Command {

    private final Dresseur dresseur;
    private final MessageStore messageStore;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public ConnectCommand(MessageStore messageStore, Dresseur dresseur) {
        this.messageStore = messageStore;
        this.dresseur = dresseur;
    }

    public Dresseur getDresseur() {
        return dresseur;
    }

    @Override
    public void execute() {
        try {
            String dresseurJson = objectMapper.writeValueAsString(dresseur);
            
            String json = "{\"interface\":\"" + Partie.class.getName() + "\"," +
                         "\"method\":\"connect\"," +
                         "\"args\":[" + dresseurJson + "]}";
            
            this.messageStore.setLastMessage(json);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing dresseur data to JSON", e);
        }
    }
}
