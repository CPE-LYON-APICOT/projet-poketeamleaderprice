package fr.cpe.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.cpe.observers.MessageObserver;
import fr.cpe.commands.AttackCommand;
import fr.cpe.commands.ChangePokemonCommand;
import fr.cpe.commands.Command;
import fr.cpe.commands.QuitCommand;
import fr.cpe.commands.UseItemCommand;
import fr.cpe.model.Attaque;
import fr.cpe.model.Dresseur;
import fr.cpe.model.Pokemon;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;

/**
 * PartieService mediator that executes commands
 */
@Singleton
public class PartieService extends CommandService {

    @Inject
    public PartieService(CommandExecutor commandExecutor, MessageStore messageStore) {
        super(commandExecutor, messageStore);
    }

    public void handleAttack(Dresseur dresseurAttaquant, Attaque attaque) {
        this.executeCommand(new AttackCommand(this.messageStore, dresseurAttaquant, attaque));
    }

    public void handleChangePokemon() {
        this.executeCommand(new ChangePokemonCommand(this.messageStore));
    }

    public void handleUseItem() {
        this.executeCommand(new UseItemCommand(this.messageStore));
    }

    public void handleQuit() {
        this.executeCommand(new QuitCommand(this.messageStore));
    }
}
