package fr.cpe.dao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fr.cpe.model.Type;

public class TypeDAO implements IDAO<Type> {

    private JSONManager jsonManager;

    public TypeDAO() {
        this.jsonManager = DBSingleton.getInstance().getJSONManager();
    }

    @Override
    public Optional<Type> get(int id) {
        try {
            JsonNode node = jsonManager.getObjectById("types", id);
            if (node != null) {
                Type type = new Type(
                    node.get("id").asInt(),
                    node.get("nom").asText()
                );
                return Optional.of(type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Type> getAll() {
        List<Type> typeList = new ArrayList<>();
        try {
            var array = jsonManager.getArray("types");
            for (JsonNode node : array) {
                Type type = new Type(
                    node.get("id").asInt(),
                    node.get("nom").asText()
                );
                typeList.add(type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return typeList;
    }

    @Override
    public void save(Type type) {
        try {
            ObjectNode node = jsonManager.getObjectMapper().createObjectNode();
            node.put("id", type.getId());
            node.put("nom", type.getNom());
            jsonManager.saveObject("types", node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Type type, String[] params) {
        save(type);
    }

    @Override
    public void delete(Type type) {
        try {
            jsonManager.deleteObject("types", type.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get weaknesses (Faiblesses) for a type
     */
    public List<Type> getFaiblesses(int typeId) {
        List<Type> faiblesses = new ArrayList<>();
        try {
            JsonNode typeNode = jsonManager.getObjectById("types", typeId);
            if (typeNode != null && typeNode.has("faiblesses")) {
                ArrayNode faiblArray = (ArrayNode) typeNode.get("faiblesses");
                for (JsonNode weaknessId : faiblArray) {
                    Optional<Type> weakness = get(weaknessId.asInt());
                    weakness.ifPresent(faiblesses::add);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return faiblesses;
    }

    /**
     * Get advantages (Avantages) for a type
     */
    public List<Type> getAvantages(int typeId) {
        List<Type> avantages = new ArrayList<>();
        try {
            JsonNode typeNode = jsonManager.getObjectById("types", typeId);
            if (typeNode != null && typeNode.has("avantages")) {
                ArrayNode avantArray = (ArrayNode) typeNode.get("avantages");
                for (JsonNode advantageId : avantArray) {
                    Optional<Type> advantage = get(advantageId.asInt());
                    advantage.ifPresent(avantages::add);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return avantages;
    }
}
