package fr.cpe.bus;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cpe.model.Attaque;
import fr.cpe.model.Pokemon;
import fr.cpe.service.Partie;
import fr.cpe.service.PartieService;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Observer that converts incoming JSON bus messages into local Partie mediator calls.
 */
public class PartieMessageObserver implements MessageObserver {

    private static final Logger LOGGER = Logger.getLogger(PartieMessageObserver.class.getName());
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final Partie partie;

    public PartieMessageObserver(Partie partie) {
        this.partie = partie;
    }

    @Override
    public boolean onMessage(String json) {
        try {
            Map<String, Object> message = OBJECT_MAPPER.readValue(json, new TypeReference<Map<String, Object>>() {});
            String interfaceName = (String) message.get("interface");
            if (interfaceName == null || !interfaceName.equals(PartieService.class.getName())) {
                return false;
            }

            String methodName = (String) message.get("method");
            List<?> args = (List<?>) message.get("args");
            if (methodName == null || args == null) {
                return false;
            }

            switch (methodName) {
                case "handleAttack":
                    if (args.size() != 3) {
                        return false;
                    }
                    Pokemon attaquant = OBJECT_MAPPER.convertValue(args.get(0), Pokemon.class);
                    Pokemon vise = OBJECT_MAPPER.convertValue(args.get(1), Pokemon.class);
                    Attaque attaque = OBJECT_MAPPER.convertValue(args.get(2), Attaque.class);
                    partie.handleAttack(attaquant, vise, attaque);
                    return true;
                case "handleChangePokemon":
                    partie.handleChangePokemon();
                    return true;
                case "handleUseItem":
                    partie.handleUseItem();
                    return true;
                case "handleQuit":
                    partie.handleQuit();
                    return true;
                default:
                    return false;
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to parse incoming message for Partie mediator", e);
            return false;
        }
    }
}
