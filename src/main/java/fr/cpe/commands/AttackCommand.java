package fr.cpe.commands;


import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cpe.model.Attaque;
import fr.cpe.model.Dresseur;
import fr.cpe.service.MessageStore;
import fr.cpe.service.Partie;

/**
 * Command for executing an attack action.
 */
public class AttackCommand implements Command {

    private final MessageStore messageStore;
    private final Dresseur dresseurAttaquant;
    private final Attaque attaque;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public AttackCommand(MessageStore messageStore, Dresseur dresseurAttaquant, Attaque attaque) {
        this.messageStore = messageStore;
        this.dresseurAttaquant = dresseurAttaquant;
        this.attaque = attaque;
    }

    @Override
    public void execute() {
        try {
            String dresseurJson = objectMapper.writeValueAsString(dresseurAttaquant);
            String attaqueJson = objectMapper.writeValueAsString(attaque);
            
            String json = "{\"interface\":\"" + Partie.class.getName() + "\"," +
                         "\"method\":\"handleAttack\"," +
                         "\"args\":[" + dresseurJson + "," + attaqueJson + "]}";
            
            this.messageStore.setLastMessage(json);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing attack data to JSON", e);
        }
    }
}
