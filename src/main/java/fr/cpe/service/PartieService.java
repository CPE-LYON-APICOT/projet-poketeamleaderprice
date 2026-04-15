package fr.cpe.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.cpe.bus.MessageObserver;
import fr.cpe.bus.commands.AttackCommand;
import fr.cpe.bus.commands.ChangePokemonCommand;
import fr.cpe.bus.commands.Command;
import fr.cpe.bus.commands.QuitCommand;
import fr.cpe.bus.commands.UseItemCommand;
import fr.cpe.model.Attaque;
import fr.cpe.model.Pokemon;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;

/**
 * PartieService mediator that executes commands and notifies observers of JSON messages.
 */
@Singleton
public class PartieService extends CommandService {

    private final List<MessageObserver> observers = new CopyOnWriteArrayList<>();

    @Inject
    public PartieService(CommandExecutor commandExecutor, MessageStore messageStore) {
        super(commandExecutor, messageStore);
    }

    /**
     * Executes a command through the mediator and notifies observers with the JSON payload.
     */
    public void executeCommand(Command command) {
        if (command == null) {
            return;
        }

        this.commandExecutor.execute(command);
        String json = this.messageStore.getLastMessage();

        if (json == null || json.isBlank()) {
            LOGGER.warning("Command did not produce a JSON message: " + command.getClass().getSimpleName());
            return;
        }

        notifyObservers(json);
    }

    public void addMessageObserver(MessageObserver observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }

    public void removeMessageObserver(MessageObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(String json) {
        for (MessageObserver observer : observers) {
            try {
                observer.onMessage(json);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error while notifying message observer", e);
            }
        }
    }

    public void handleAttack(Pokemon pokemonAttaquant, Pokemon pokemonVise, Attaque attaque) {
        executeCommand(new AttackCommand(this.messageStore, pokemonAttaquant, pokemonVise, attaque));
    }

    public void handleChangePokemon() {
        executeCommand(new ChangePokemonCommand(this.messageStore));
    }

    public void handleUseItem() {
        executeCommand(new UseItemCommand(this.messageStore));
    }

    public void handleQuit() {
        executeCommand(new QuitCommand(this.messageStore));
    }
}
