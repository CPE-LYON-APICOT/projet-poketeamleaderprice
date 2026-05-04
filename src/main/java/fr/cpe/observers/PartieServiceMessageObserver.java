package fr.cpe.observers;

import com.fasterxml.jackson.core.type.TypeReference;

import fr.cpe.dao.AttaqueDAO;
import fr.cpe.model.Attaque;
import fr.cpe.model.Dresseur;
import fr.cpe.service.Partie;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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
            if (methodName == null || args == null) {
                return false;
            }

            switch (methodName) {
                case "handleAttack":
                    if (args.size() != 2) return false;

                    // args.get(0) est le Dresseur complet — extraire son index
                    Dresseur dresseurAttaquant = OBJECT_MAPPER.convertValue(args.get(0), Dresseur.class);
                    
                    // args.get(1) est l'Attaque complète — la désérialiser directement
                    Attaque attaqueDeserialisee = OBJECT_MAPPER.convertValue(args.get(1), Attaque.class);

                    this.partie.attack(
                        this.partie.getDresseurFromId(dresseurAttaquant.getIndex()),
                        attaqueDeserialisee
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
