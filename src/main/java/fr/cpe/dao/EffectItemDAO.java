package fr.cpe.dao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import fr.cpe.model.EffectItem;
import fr.cpe.model.Item;
import fr.cpe.model.StatType;

public class EffectItemDAO implements IDAO<EffectItem> {

    private final JSONManager jsonManager;

    public EffectItemDAO() {
        this.jsonManager = DBSingleton.getInstance().getJSONManager();
    }

    @Override
    public Optional<EffectItem> get(int id) {
        try {
            JsonNode node = jsonManager.getObjectById("effectItems", id);
            if (node != null) {
                Map<StatType, Integer> stats = new HashMap<>();
                if (node.has("affectedStat")) {
                    JsonNode statsNode = node.get("affectedStat");
                    if (statsNode.has("Atk")) stats.put(StatType.Atk, statsNode.get("Atk").asInt());
                    if (statsNode.has("AtkSpe")) stats.put(StatType.AtkSpe, statsNode.get("AtkSpe").asInt());
                    if (statsNode.has("Def")) stats.put(StatType.Def, statsNode.get("Def").asInt());
                    if (statsNode.has("DefSpe")) stats.put(StatType.DefSpe, statsNode.get("DefSpe").asInt());
                    if (statsNode.has("Spd")) stats.put(StatType.Spd, statsNode.get("Spd").asInt());
                }
                EffectItem effectItem = new EffectItem(
                    node.get("id").asInt(),
                    node.get("nom").asText(),
                    stats
                );
                return Optional.of(effectItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<EffectItem> getAll() {
        List<EffectItem> itemList = new ArrayList<>();
        try {
            var array = jsonManager.getArray("effectItems");
            for (JsonNode node : array) {
                Map<StatType, Integer> stats = new HashMap<>();
                if (node.has("affectedStat")) {
                    JsonNode statsNode = node.get("affectedStat");
                    if (statsNode.has("Atk")) stats.put(StatType.Atk, statsNode.get("Atk").asInt());
                    if (statsNode.has("AtkSpe")) stats.put(StatType.AtkSpe, statsNode.get("AtkSpe").asInt());
                    if (statsNode.has("Def")) stats.put(StatType.Def, statsNode.get("Def").asInt());
                    if (statsNode.has("DefSpe")) stats.put(StatType.DefSpe, statsNode.get("DefSpe").asInt());
                    if (statsNode.has("Spd")) stats.put(StatType.Spd, statsNode.get("Spd").asInt());
                }
                itemList.add(new EffectItem(
                    node.get("id").asInt(),
                    node.get("nom").asText(),
                    stats
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemList;
    }

    @Override
    public void save(EffectItem item) {
        try {
            ObjectNode node = jsonManager.getObjectMapper().createObjectNode();
            node.put("id", item.getId());
            node.put("nom", item.getNom());
            
            ObjectNode statsNode = jsonManager.getObjectMapper().createObjectNode();
            for (Map.Entry<StatType, Integer> entry : item.getAffectedStat().entrySet()) {
                statsNode.put(entry.getKey().toString(), entry.getValue());
            }
            node.set("affectedStat", statsNode);
            
            jsonManager.saveObject("effectItems", node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(EffectItem item, String[] params) {
        save(item);
    }

    @Override
    public void delete(EffectItem item) {
        try {
            jsonManager.deleteObject("effectItems", item.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
