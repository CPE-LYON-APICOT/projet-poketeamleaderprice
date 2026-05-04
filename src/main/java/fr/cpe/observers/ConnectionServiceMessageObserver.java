package fr.cpe.observers;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import com.fasterxml.jackson.core.type.TypeReference;
import fr.cpe.model.Dresseur;
import fr.cpe.model.Stade;
import fr.cpe.service.Partie;

public class ConnectionServiceMessageObserver extends MessageObserver {

    public ConnectionServiceMessageObserver(Partie partie) {
        super(partie);
    }

    @Override
    public boolean onMessage(String json) {
        LOGGER.info("[ConnectionServiceMessageObserver] received message: " + json + "\n\n\n");
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

            String dresseurJson;

            switch (methodName) {
                case "connect":
                    if (args.size() != 1) {
                        return false;
                    }
                    dresseurJson = OBJECT_MAPPER.writeValueAsString(args.get(0));
                    Dresseur dresseur2 = OBJECT_MAPPER.readValue(dresseurJson, Dresseur.class);
                    this.partie.setDresseur2(dresseur2);
                    this.partie.setActivePokemonOf(dresseur2, dresseur2.getPokemonTeam(0));
                    return true;
                case "hostGame":
                    if (args.size() != 2) {
                        return false;
                    }
                    dresseurJson = OBJECT_MAPPER.writeValueAsString(args.get(0));
                    Dresseur dresseur1 = OBJECT_MAPPER.readValue(dresseurJson, Dresseur.class);
                    this.partie.setDresseur1(dresseur1);
                    this.partie.setActivePokemonOf(dresseur1, dresseur1.getPokemonTeam(0));
                    this.partie.setStade(OBJECT_MAPPER.convertValue(args.get(1), Stade.class));
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
