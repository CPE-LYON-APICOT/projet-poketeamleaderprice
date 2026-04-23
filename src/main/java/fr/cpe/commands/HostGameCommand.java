package fr.cpe.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.cpe.model.Dresseur;
import fr.cpe.model.Stade;
import fr.cpe.service.MessageStore;
import fr.cpe.service.Partie;

public class HostGameCommand implements Command {

    private final Dresseur dresseur;
    private final Stade stade;
    private MessageStore messageStore;

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
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("method", "hostGame");
        jsonNode.putPOJO("args", new Object[]{
            this.dresseur,
            Partie.getInstance()
                .setDresseur1(dresseur)
                .setStade(this.stade)
        });
        String json = jsonNode.toString();
        this.messageStore.setLastMessage(json);
    }
}
