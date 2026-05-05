package fr.cpe.dao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fr.cpe.model.Stade;

public class StadeDAO implements IDAO<Stade> {

    private JSONManager jsonManager;

    public StadeDAO() {
        this.jsonManager = DBSingleton.getInstance().getJSONManager();
    }

    @Override
    public Optional<Stade> get(int id) {
        try {
            JsonNode node = jsonManager.getObjectById("stades", id);
            if (node != null) {
                Stade stade = new Stade(
                    node.get("id").asInt(),
                    node.get("nom").asText(),
                    new TypeDAO().get(node.get("type_id").asInt()).orElse(null)
                );
                return Optional.of(stade);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Stade> getAll() {
        List<Stade> stadeList = new ArrayList<>();
        try {
            var array = jsonManager.getArray("stades");
            for (JsonNode node : array) {
                stadeList.add(new Stade(
                    node.get("id").asInt(),
                    node.get("nom").asText(),
                    new TypeDAO().get(node.get("type_id").asInt()).orElse(null)
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stadeList;
    }

    @Override
    public void save(Stade stade) {
        try {
            ObjectNode node = jsonManager.getObjectMapper().createObjectNode();
            node.put("id", stade.getId());
            node.put("nom", stade.getNom());
            node.put("type_id", stade.getType() != null ? stade.getType().getId() : 0);
            jsonManager.saveObject("stades", node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Stade stade, String[] params) {
        save(stade);
    }

    @Override
    public void delete(Stade stade) {
        try {
            jsonManager.deleteObject("stades", stade.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
