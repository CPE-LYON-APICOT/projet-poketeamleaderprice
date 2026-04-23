package fr.cpe.dao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.*;

import fr.cpe.model.HealingItem;
import fr.cpe.model.StatType;

public class HealingItemDAO implements IDAO<HealingItem> {

    private JSONManager jsonManager;

    public HealingItemDAO() {
        this.jsonManager = DBSingleton.getInstance().getJSONManager();
    }

    @Override
    public Optional<HealingItem> get(int id) {
        try {
            JsonNode node = jsonManager.getObjectById("healingItems", id);
            if (node != null) {
                HealingItem healingItem = new HealingItem(
                    node.get("id").asInt(),
                    node.get("nom").asText(),
                    node.get("hpHeal").asInt()
                );
                return Optional.of(healingItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<HealingItem> getAll() {
        List<HealingItem> itemList = new ArrayList<>();
        try {
            var array = jsonManager.getArray("healingItems");
            for (JsonNode node : array) {
                HealingItem healingItem = new HealingItem(
                    node.get("id").asInt(),
                    node.get("nom").asText(),
                    node.get("hpHeal").asInt()
                );
                itemList.add(healingItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemList;
    }

    public List<String> getAllNom() {
        List<String> noms = new ArrayList<>();
        try {
            var array = jsonManager.getArray("healingItems");
            for (JsonNode node : array) {
                Map<StatType, Integer> stats = new HashMap<>();
                noms.add(node.get("nom").asText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return noms;
    }

    @Override
    public void save(HealingItem item) {
        try {
            ObjectNode node = jsonManager.getObjectMapper().createObjectNode();
            node.put("id", item.getId());
            node.put("nom", item.getNom());
            node.put("hpHeal", item.getHpHeal());
            jsonManager.saveObject("healingItems", node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(HealingItem item, String[] params) {
        save(item);
    }

    @Override
    public void delete(HealingItem item) {
        try {
            jsonManager.deleteObject("healingItems", item.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
