package fr.cpe.service;
import com.google.inject.Singleton;
import fr.cpe.App;

/**
 * Implementation of PartieService.
 * Service for managing game logic related to Partie.
 * Acts as the receiver for Command objects.
 */
@Singleton
public class PartieServiceImpl implements PartieService {

    private final MessageStore messageStore = App.injector.getInstance(MessageStore.class);
    private final CommandExecutor commandExecutor = new CommandExecutor();
    private PartieServiceImpl instance;

    private PartieServiceImpl() {}

    public PartieServiceImpl getInstance() {
        if(this.instance == null) {
            return new PartieServiceImpl();
        }
        return this.instance;
    }

    @Override
    public void handleAttack() {
        this.commandExecutor.execute(new AttackCommand(messageStore));
    }

    @Override
    public void handleChangePokemon() {
        this.commandExecutor.execute(new ChangePokemonCommand(messageStore));
    }

    @Override
    public void handleUseItem() {
        this.commandExecutor.execute(new UseItemCommand(messageStore));
    }

    @Override
    public void handleQuit() {
        this.commandExecutor.execute(new QuitCommand(messageStore));
    }
}