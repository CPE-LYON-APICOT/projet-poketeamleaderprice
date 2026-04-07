package fr.cpe.dao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import fr.cpe.model.Attaque;
import fr.cpe.model.Pokemon;
import fr.cpe.model.StatType;
import fr.cpe.model.Type;

public class PokemonDAO implements IDAO<Pokemon> {

    private JSONManager jsonManager;
    private TypeDAO typeDAO;
    private AttaqueDAO attaqueDAO;
    private AbiliteDAO abiliteDAO;

    public PokemonDAO() {
        this.jsonManager = DBSingleton.getInstance().getJSONManager();
        this.typeDAO = new TypeDAO();
        this.attaqueDAO = new AttaqueDAO();
        this.abiliteDAO = new AbiliteDAO();
    }

    @Override
    public Optional<Pokemon> get(int id) {
        try {
            JsonNode node = jsonManager.getObjectById("pokemons", id);
            if (node != null) {
                Pokemon pokemon = buildPokemonFromNode(node);
                return Optional.of(pokemon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Pokemon> getAll() {
        List<Pokemon> pokemonList = new ArrayList<>();
        try {
            var array = jsonManager.getArray("pokemons");
            for (JsonNode node : array) {
                pokemonList.add(buildPokemonFromNode(node));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pokemonList;
    }

    @Override
    public void save(Pokemon pokemon) {
        try {
            ObjectNode node = jsonManager.getObjectMapper().createObjectNode();
            node.put("id", pokemon.getNum_Poke());
            node.put("name", pokemon.getNom());
            node.put("hp", pokemon.getHp());
            node.put("imageFacePath", pokemon.getImage_face());
            node.put("imageDosPath", pokemon.getImage_dos());
            node.put("imageSpritePath", pokemon.getSprite());
            node.put("description", pokemon.getDescription());
            
            // Store ability ID
            if (pokemon.getAbility() != null) {
                node.put("abilityId", pokemon.getAbility().getId());
            } else {
                node.put("abilityId", 0);
            }
            
            // Store types as array of IDs
            ArrayNode typesArray = jsonManager.getObjectMapper().createArrayNode();
            for (Type type : pokemon.getTypes()) {
                typesArray.add(type.getId());
            }
            node.set("types", typesArray);
            
            // Store available attacks as array of IDs
            ArrayNode availableAttacksArray = jsonManager.getObjectMapper().createArrayNode();
            for (Attaque attack : pokemon.getLesattaquesdisponibles()) {
                availableAttacksArray.add(attack.getId());
            }
            node.set("availableAttacks", availableAttacksArray);
            
            // Store learned attacks as array of IDs
            ArrayNode learnedAttacksArray = jsonManager.getObjectMapper().createArrayNode();
            for (Attaque attack : pokemon.getLesattaquesprises()) {
                learnedAttacksArray.add(attack.getId());
            }
            node.set("learnedAttacks", learnedAttacksArray);
            
            // Store stats
            ObjectNode statsNode = jsonManager.getObjectMapper().createObjectNode();
            for (Map.Entry<StatType, Integer> entry : pokemon.getStats().entrySet()) {
                statsNode.put(entry.getKey().toString(), entry.getValue());
            }
            node.set("stats", statsNode);
            
            jsonManager.saveObject("pokemons", node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Pokemon pokemon, String[] params) {
        save(pokemon);
    }

    @Override
    public void delete(Pokemon pokemon) {
        try {
            jsonManager.deleteObject("pokemons", pokemon.getNum_Poke());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Build a Pokemon object from a JSON node
     */
    private Pokemon buildPokemonFromNode(JsonNode node) {
        int id = node.get("id").asInt();
        String name = node.get("name").asText();
        int hp = node.get("hp").asInt();
        String imageFace = node.get("imageFacePath").asText();
        String imageDos = node.get("imageDosPath").asText();
        String sprite = node.get("imageSpritePath").asText();
        String description = node.get("description").asText();
        
        // Get types
        List<Type> types = getTypes(id, node);
        
        // Get available attacks
        Attaque[] availableAttacks = getLesAttaquesDisponibles(id, node);
        
        // Get learned attacks
        Attaque[] learnedAttacks = getLesAttaquesPrises(id, node);
        
        // Get ability
        int abilityId = node.has("abilityId") ? node.get("abilityId").asInt() : 0;
        var ability = abilityId > 0 ? abiliteDAO.get(abilityId).orElse(null) : null;
        
        // Get stats
        Map<StatType, Integer> stats = new HashMap<>();
        if (node.has("stats")) {
            JsonNode statsNode = node.get("stats");
            if (statsNode.has("Atk")) stats.put(StatType.Atk, statsNode.get("Atk").asInt());
            if (statsNode.has("AtkSpe")) stats.put(StatType.AtkSpe, statsNode.get("AtkSpe").asInt());
            if (statsNode.has("Def")) stats.put(StatType.Def, statsNode.get("Def").asInt());
            if (statsNode.has("DefSpe")) stats.put(StatType.DefSpe, statsNode.get("DefSpe").asInt());
            if (statsNode.has("Spd")) stats.put(StatType.Spd, statsNode.get("Spd").asInt());
        }
        
        return new Pokemon(id, name, types, availableAttacks, learnedAttacks, hp, stats, 
                          imageFace, imageDos, sprite, description, ability);
    }

    /**
     * Get types for a Pokemon from the node
     */
    private List<Type> getTypes(int pokemonId, JsonNode node) {
        List<Type> types = new ArrayList<>();
        try {
            if (node.has("types") && node.get("types").isArray()) {
                ArrayNode typesArray = (ArrayNode) node.get("types");
                for (JsonNode typeIdNode : typesArray) {
                    int typeId = typeIdNode.asInt();
                    Optional<Type> type = typeDAO.get(typeId);
                    type.ifPresent(types::add);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return types;
    }

    /**
     * Get available attacks for a Pokemon from the node
     */
    private Attaque[] getLesAttaquesDisponibles(int pokemonId, JsonNode node) {
        List<Attaque> attaques = new ArrayList<>();
        try {
            if (node.has("availableAttacks") && node.get("availableAttacks").isArray()) {
                ArrayNode attacksArray = (ArrayNode) node.get("availableAttacks");
                for (JsonNode attackIdNode : attacksArray) {
                    int attackId = attackIdNode.asInt();
                    Optional<Attaque> attack = attaqueDAO.get(attackId);
                    attack.ifPresent(attaques::add);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return attaques.toArray(new Attaque[0]);
    }

    /**
     * Get learned attacks for a Pokemon from the node
     */
    private Attaque[] getLesAttaquesPrises(int pokemonId, JsonNode node) {
        List<Attaque> attaques = new ArrayList<>();
        try {
            if (node.has("learnedAttacks") && node.get("learnedAttacks").isArray()) {
                ArrayNode attacksArray = (ArrayNode) node.get("learnedAttacks");
                for (JsonNode attackIdNode : attacksArray) {
                    int attackId = attackIdNode.asInt();
                    Optional<Attaque> attack = attaqueDAO.get(attackId);
                    attack.ifPresent(attaques::add);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return attaques.toArray(new Attaque[0]);
    }
}
