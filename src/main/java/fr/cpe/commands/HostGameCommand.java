package fr.cpe.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.cpe.model.Dresseur;
import fr.cpe.model.Stade;
import fr.cpe.service.MessageStore;

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
        jsonNode.put("interface", "fr.cpe.service.Partie");
        jsonNode.put("method", "hostGame");
        com.fasterxml.jackson.databind.node.ArrayNode paramTypes = objectMapper.createArrayNode();
        paramTypes.add("fr.cpe.model.Dresseur");
        jsonNode.set("paramTypes", paramTypes);
        com.fasterxml.jackson.databind.node.ArrayNode args = objectMapper.createArrayNode();
        args.addPOJO(this.dresseur);
        jsonNode.set("args", args);
        String json = jsonNode.toString();
        this.messageStore.setLastMessage(json);
    }
}
