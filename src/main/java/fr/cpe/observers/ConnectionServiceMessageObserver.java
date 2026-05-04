package fr.cpe.observers;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import com.fasterxml.jackson.core.type.TypeReference;
import fr.cpe.model.Dresseur;
import fr.cpe.bus.InstanceIdentity;
import fr.cpe.service.Partie;

public class ConnectionServiceMessageObserver extends MessageObserver {

    @Override
    public boolean onMessage(String json) {
        try {
            Map<String, Object> message = OBJECT_MAPPER.readValue(json, new TypeReference<>() {});
            // Log raw incoming message and sender for diagnostics
            String sender = (String) message.get("sender");
            String self = InstanceIdentity.get();
            LOGGER.info(() -> "ConnectionServiceMessageObserver incoming json=" + json + " sender=" + sender + " self=" + self);
            // Ignore messages that we ourselves sent (prevents double-processing)
            if (sender != null && sender.equals(self)) {
                LOGGER.info(() -> "ConnectionServiceMessageObserver ignoring self-sent message (sender=" + sender + ")");
                return true;
            }
            String interfaceName = (String) message.get("interface");
            if (interfaceName == null || !interfaceName.equals(Partie.class.getName())) {
                return false;
            }

            String methodName = (String) message.get("method");
            List<?> args = (List<?>) message.get("args");
            LOGGER.info(() -> "ConnectionServiceMessageObserver received method=" + methodName + " args=" + (args == null ? "null" : args.toString()));
            if (methodName == null || args == null) {
                return false;
            }

            switch (methodName) {
                case "connect":
                    if (args.size() != 1) {
                        return false;
                    }
                    Partie.getInstance().setDresseur2(OBJECT_MAPPER.convertValue(args.getFirst(), Dresseur.class));
                    return true;
                case "hostGame":
                    if (args.size() != 1) {
                        return false;
                    }
                    Partie.getInstance().setDresseur1(OBJECT_MAPPER.convertValue(args.getFirst(), Dresseur.class));
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
