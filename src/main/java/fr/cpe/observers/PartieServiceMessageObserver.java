package fr.cpe.observers;

import com.fasterxml.jackson.core.type.TypeReference;

import fr.cpe.model.Attaque;
import fr.cpe.model.Dresseur;
import fr.cpe.model.Pokemon;
import fr.cpe.service.Partie;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Observer that converts incoming JSON bus messages into local Partie mediator calls.
 */
public class PartieServiceMessageObserver extends MessageObserver {

    public PartieServiceMessageObserver(Partie partie) {
        super(partie);
    }

    @Override
    public boolean onMessage(String json) {
        LOGGER.info("[PartieServiceMessageObserver] received message: " + json);
        try {
            Map<String, Object> message = OBJECT_MAPPER.readValue(json, new TypeReference<Map<String, Object>>() {});
            String interfaceName = (String) message.get("interface");
            if (interfaceName == null || !interfaceName.equals(Partie.class.getName())) {
                return false;
            }

            String methodName = (String) message.get("method");
            List<?> args = (List<?>) message.get("args");
            if (methodName == null) {
                return false;
            }

            switch (methodName) {
                case "handleAttack": {
                    if (args == null || args.size() != 2) return false;

                    Dresseur dresseurAttaquant = OBJECT_MAPPER.convertValue(args.get(0), Dresseur.class);
                    Attaque attaqueDeserialisee = OBJECT_MAPPER.convertValue(args.get(1), Attaque.class);

                    this.partie.attack(
                        this.partie.getDresseurFromId(dresseurAttaquant.getIndex()),
                        attaqueDeserialisee
                    );
                    return true;
                }

                case "handleChangePokemon": {
                    if (args == null || args.size() != 2) return false;

                    Dresseur dresseurChanger = OBJECT_MAPPER.convertValue(args.get(0), Dresseur.class);
                    Pokemon  pokemonRef      = OBJECT_MAPPER.convertValue(args.get(1), Pokemon.class);

                    Dresseur dresseurReel = this.partie.getDresseurFromId(dresseurChanger.getIndex());

                    // On retrouve le vrai objet dans l'équipe via son numéro de Pokédex
                    Pokemon pokemonReel = dresseurReel.getPokemon().values().stream()
                        .filter(p -> p.getNum_Poke().equals(pokemonRef.getNum_Poke()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException(
                            "Pokémon #" + pokemonRef.getNum_Poke() + " non trouvé dans l'équipe"));

                    this.partie.changePokemon(dresseurReel, pokemonReel);
                    return true;
                }

                case "handleUseItem":
                    this.partie.useItem();
                    return true;

                case "handleQuit": {
                    if (args == null || args.size() != 1) return false;
                    Dresseur dresseurFuite = OBJECT_MAPPER.convertValue(args.get(0), Dresseur.class);
                    this.partie.quit();
                    return true;
                }

                default:
                    return false;
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to parse incoming message for PartieService mediator", e);
            return false;
        }
    }
}
