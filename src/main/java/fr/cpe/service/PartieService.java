package fr.cpe.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
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
    public PartieService(CommandExecutor commandExecutor, MessageStore messageStore) {
        super(commandExecutor, messageStore);
    }

    public void handleAttack(Dresseur dresseurAttaquant, Attaque attaque) {
        this.executeCommand(new AttackCommand(this.messageStore, dresseurAttaquant, attaque));
    }

    /**
     * Envoie une commande de changement de Pokémon actif sur le bus.
     *
     * @param dresseur      le dresseur qui change de Pokémon
     * @param nouveauPokemon le Pokémon choisi en remplacement
     */
    public void handleChangePokemon(Dresseur dresseur, Pokemon nouveauPokemon) {
        this.executeCommand(new ChangePokemonCommand(this.messageStore, dresseur, nouveauPokemon));
    }

    public void handleUseItem() {
        this.executeCommand(new UseItemCommand(this.messageStore));
    }

    public void handleQuit(Dresseur dresseur) {
        this.executeCommand(new QuitCommand(this.messageStore, dresseur));
    }
}
