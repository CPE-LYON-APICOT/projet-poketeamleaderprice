package fr.cpe.observers;

import com.fasterxml.jackson.core.type.TypeReference;

import fr.cpe.dao.AttaqueDAO;
import fr.cpe.model.Attaque;
import fr.cpe.model.Dresseur;
import fr.cpe.model.Pokemon;
import fr.cpe.service.Partie;
import fr.cpe.service.PartieService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

/**
 * Observer that converts incoming JSON bus messages into local Partie mediator calls.
 */
public class PartieServiceMessageObserver extends MessageObserver {

    private final Partie partie;

    public PartieServiceMessageObserver(Partie partie) {
        this.partie = partie;
    }

    @Override
    public boolean onMessage(String json) {
        try {
            Map<String, Object> message = OBJECT_MAPPER.readValue(json, new TypeReference<Map<String, Object>>() {});
            String interfaceName = (String) message.get("interface");
            if (interfaceName == null || !interfaceName.equals(Partie.class.getName())) {
                return false;
            }

            String methodName = (String) message.get("method");
            List<?> args = (List<?>) message.get("args");
            if (methodName == null || args == null) {
                return false;
            }

            switch (methodName) {
                case "handleAttack":
                    if (args.size() != 2) {
                        return false;
                    }
                    Optional<Attaque> attaque = new AttaqueDAO().get(OBJECT_MAPPER.convertValue(args.get(1), Integer.class));
                    this.partie.attack(
                        this.partie.getDresseurFromId(OBJECT_MAPPER.convertValue(args.get(0), Integer.class)),
                        attaque.orElseThrow(() -> new IllegalArgumentException("Invalid attack ID: " + args.get(1)))
                    );
                    return true;
                case "handleChangePokemon":
                    return true;
                case "handleUseItem":
                    this.partie.useItem();
                    return true;
                case "handleQuit":
                    this.partie.quit();
                    return true;
                default:
                    return false;
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to parse incoming message for PartieService mediator", e);
            return false;
        }
    }
}
