package fr.cpe.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cpe.model.Dresseur;
import fr.cpe.model.Pokemon;
import fr.cpe.service.MessageStore;
import fr.cpe.service.Partie;

/**
 * Command for changing the active Pokémon.
 * Serializes the trainer and the new Pokémon to JSON and puts it on the message bus.
 */
public class ChangePokemonCommand implements Command {

    private final MessageStore messageStore;
    private final Dresseur dresseur;
    private final Pokemon nouveauPokemon;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public ChangePokemonCommand(MessageStore messageStore, Dresseur dresseur, Pokemon nouveauPokemon) {
        this.messageStore = messageStore;
        this.dresseur = dresseur;
        this.nouveauPokemon = nouveauPokemon;
    }

    @Override
    public void execute() {
        try {
            String dresseurJson = objectMapper.writeValueAsString(dresseur);
            String pokemonJson  = objectMapper.writeValueAsString(nouveauPokemon);

            String json = "{\"interface\":\"" + Partie.class.getName() + "\"," +
                         "\"method\":\"handleChangePokemon\"," +
                         "\"args\":[" + dresseurJson + "," + pokemonJson + "]}";

            this.messageStore.setLastMessage(json);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing changePokemon data to JSON", e);
        }
    }
}
