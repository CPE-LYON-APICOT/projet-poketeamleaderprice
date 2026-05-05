package fr.cpe.commands;

import fr.cpe.model.Dresseur;
import fr.cpe.service.MessageStore;
import fr.cpe.service.Partie;

public class DisconnectCommand implements Command {

    private final Dresseur dresseur;
    private MessageStore messageStore;

    public DisconnectCommand(MessageStore messageStore, Dresseur dresseur) {
        this.messageStore = messageStore;
        this.dresseur = dresseur;
    }

    public Dresseur getDresseur() {
        return dresseur;
    }

    @Override
    public void execute() {
    }
}
