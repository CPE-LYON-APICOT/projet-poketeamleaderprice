package fr.cpe.commands;

import fr.cpe.model.Dresseur;
import fr.cpe.service.MessageStore;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConnectCommand implements Command {

    private final Dresseur dresseur;
    private MessageStore messageStore;

    public ConnectCommand(MessageStore messageStore, Dresseur dresseur) {
        this.messageStore = messageStore;
        this.dresseur = dresseur;
    }

    public Dresseur getDresseur() {
        return dresseur;
    }

    @Override
    public void execute() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("method", "connect");
        jsonNode.putPOJO("args", new Dresseur[]{this.dresseur});
        String json = jsonNode.toString();
        this.messageStore.setLastMessage(json);
    }

}
