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
import java.util.logging.Logger;

/**
 * Partie mediator that executes commands and notifies observers of JSON messages.
 */
@Singleton
public class Partie implements PartieService {

    private static final Logger LOGGER = Logger.getLogger(Partie.class.getName());
    private final CommandExecutor commandExecutor;
    private final MessageStore messageStore;
    private final List<MessageObserver> observers = new CopyOnWriteArrayList<>();

    @Inject
    public Partie(CommandExecutor commandExecutor, MessageStore messageStore) {
        this.commandExecutor = commandExecutor;
        this.messageStore = messageStore;
    }

    /**
     * Executes a command through the mediator and notifies observers with the JSON payload.
     */
    public void executeCommand(Command command) {
        if (command == null) {
            return;
        }

        commandExecutor.execute(command);
        String json = messageStore.getLastMessage();

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

    @Override
    public void handleAttack(Pokemon pokemonAttaquant, Pokemon pokemonVise, Attaque attaque) {
        executeCommand(new AttackCommand(messageStore, pokemonAttaquant, pokemonVise, attaque));
    }

    @Override
    public void handleChangePokemon() {
        executeCommand(new ChangePokemonCommand(messageStore));
    }

    @Override
    public void handleUseItem() {
        executeCommand(new UseItemCommand(messageStore));
    }

    @Override
    public void handleQuit() {
        executeCommand(new QuitCommand(messageStore));
    }
}
