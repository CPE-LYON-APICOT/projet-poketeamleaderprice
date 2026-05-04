package fr.cpe.dao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fr.cpe.model.Abilite;

public class AbiliteDAO implements IDAO<Abilite> {

    private JSONManager jsonManager;

    public AbiliteDAO() {
        this.jsonManager = DBSingleton.getInstance().getJSONManager();
    }

    @Override
    public Optional<Abilite> get(int id) {
        try {
            JsonNode node = jsonManager.getObjectById("abilites", id);
            if (node != null) {
                Abilite abilite = new Abilite(
                    node.get("id").asInt(),
                    node.get("nom").asText(),
                    node.get("description").asText()
                );
                return Optional.of(abilite);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Abilite> getAll() {
        List<Abilite> abiliteList = new ArrayList<>();
        try {
            var array = jsonManager.getArray("abilites");
            for (JsonNode node : array) {
                Abilite abilite = new Abilite(
                    node.get("id").asInt(),
                    node.get("nom").asText(),
                    node.get("description").asText()
                );
                abiliteList.add(abilite);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return abiliteList;
    }

    @Override
    public void save(Abilite abilite) {
        try {
            ObjectNode node = jsonManager.getObjectMapper().createObjectNode();
            node.put("id", abilite.getId());
            node.put("nom", abilite.getNom());
            node.put("description", abilite.getDescription());
            jsonManager.saveObject("abilites", node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Abilite abilite, String[] params) {
        save(abilite);
    }

    @Override
    public void delete(Abilite abilite) {
        try {
            jsonManager.deleteObject("abilites", abilite.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
