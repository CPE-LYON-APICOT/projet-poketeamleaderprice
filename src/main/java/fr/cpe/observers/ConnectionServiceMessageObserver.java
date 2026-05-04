package fr.cpe.observers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import com.fasterxml.jackson.core.type.TypeReference;
import fr.cpe.dao.AttaqueDAO;
import fr.cpe.model.Attaque;
import fr.cpe.model.Dresseur;
import fr.cpe.service.Partie;

public class ConnectionServiceMessageObserver extends MessageObserver {

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
                case "connect":
                    if (args.size() != 1) {
                        return false;
                    }
                    Partie.getInstance().setDresseur2(OBJECT_MAPPER.convertValue(args.get(0), Dresseur.class));
                    return true;
                case "hostGame":
                    if (args.size() != 1) {
                        return false;
                    }
                    Partie.getInstance().setDresseur1(OBJECT_MAPPER.convertValue(args.get(0), Dresseur.class));
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
