package fr.cpe.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.azure.messaging.webpubsub.WebPubSubServiceClient;
import fr.cpe.commands.AttackCommand;
import fr.cpe.commands.ChangePokemonCommand;
import fr.cpe.commands.QuitCommand;
import fr.cpe.commands.UseItemCommand;
import fr.cpe.model.Attaque;
import fr.cpe.model.Dresseur;
import fr.cpe.model.Pokemon;

/**
 * PartieService mediator that executes commands
 */
@Singleton
public class PartieService extends CommandService {

    @Inject
    public PartieService(CommandExecutor commandExecutor, MessageStore messageStore, WebPubSubServiceClient publisher) {
        super(commandExecutor, messageStore, publisher);
    }

    public void handleAttack(Dresseur dresseurAttaquant, Attaque attaque) {
        this.executeCommand(new AttackCommand(this.messageStore, dresseurAttaquant, attaque));
    }

    public void handleChangePokemon(Dresseur dresseur, Pokemon nouveauPokemon) {
        this.executeCommand(new ChangePokemonCommand(this.messageStore, dresseur, nouveauPokemon));
    }

    public void handleUseItem() {
        this.executeCommand(new UseItemCommand(this.messageStore));
    }

    public void handleQuit() {
        this.executeCommand(new QuitCommand(this.messageStore));
    }
}
