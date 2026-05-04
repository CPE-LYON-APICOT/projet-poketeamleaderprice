package fr.cpe.dao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import java.util.Optional;

import fr.cpe.model.Attaque;

public class AttaqueDAO implements IDAO<Attaque> {

    private JSONManager jsonManager;

    public AttaqueDAO() {
        this.jsonManager = DBSingleton.getInstance().getJSONManager();
    }

    @Override
    public Optional<Attaque> get(int id) {
        try {
            JsonNode node = jsonManager.getObjectById("attaques", id);
            if (node != null) {
                Attaque attaque = new Attaque(
                    node.get("id").asInt(),
                    node.get("name").asText(),
                    node.get("power").asInt(),
                    node.get("accuracy").asInt(),
                    node.get("pp").asInt(),
                    new TypeDAO().get(node.get("type_id").asInt()).orElse(null)
                );
                return Optional.of(attaque);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Attaque> getAll() {
        List<Attaque> attaqueList = new java.util.ArrayList<>();
        try {
            var array = jsonManager.getArray("attaques");
            for (JsonNode node : array) {
                Attaque attaque = new Attaque(
                    node.get("id").asInt(),
                    node.get("name").asText(),
                    node.get("power").asInt(),
                    node.get("accuracy").asInt(),
                    node.get("pp").asInt(),
                    new TypeDAO().get(node.get("type_id").asInt()).orElse(null)
                );
                attaqueList.add(attaque);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return attaqueList;
    }

    @Override
    public void save(Attaque attaque) {
        try {
            ObjectNode node = jsonManager.getObjectMapper().createObjectNode();
            node.put("id", attaque.getId());
            node.put("name", attaque.getName());
            node.put("power", attaque.getPower());
            node.put("accuracy", attaque.getAccuracy());
            node.put("pp", attaque.getPp());
            node.put("type_id", attaque.getType() != null ? attaque.getType().getId() : 0);
            jsonManager.saveObject("attaques", node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Attaque attaque, String[] params) {
        save(attaque);
    }

    @Override
    public void delete(Attaque attaque) {
        try {
            jsonManager.deleteObject("attaques", attaque.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
