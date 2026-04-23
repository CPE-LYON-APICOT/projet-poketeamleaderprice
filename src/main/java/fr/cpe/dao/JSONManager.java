package fr.cpe.dao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Manages JSON database operations for Pokémon app
 * Handles reading and writing data from/to db.json
 */
public class JSONManager {

    private static JSONManager instance;
    private ObjectMapper objectMapper;
    private String dbPath;
    private JsonNode rootNode;

    private JSONManager() {
        try {
            this.objectMapper = new ObjectMapper();
            Dotenv dotenv = Dotenv.load();
            this.dbPath = dotenv.get("DB_PATH", "db/db.json");
            
            loadDatabase();
        } catch (Exception e) {
            System.out.println("Error initializing JSONManager: " + e);
            e.printStackTrace();
        }
    }

    public static JSONManager getInstance() {
        if (instance == null) {
            instance = new JSONManager();
        }
        return instance;
    }

    /**
     * Load the entire database from JSON file
     */
    private void loadDatabase() {
        try {
            File file = new File(dbPath);
            if (!file.exists()) {
                // Create empty database structure
                rootNode = objectMapper.createObjectNode();
                ((ObjectNode) rootNode).set("pokemons", objectMapper.createArrayNode());
                ((ObjectNode) rootNode).set("types", objectMapper.createArrayNode());
                ((ObjectNode) rootNode).set("attaques", objectMapper.createArrayNode());
                ((ObjectNode) rootNode).set("abilites", objectMapper.createArrayNode());
                ((ObjectNode) rootNode).set("effectItems", objectMapper.createArrayNode());
                ((ObjectNode) rootNode).set("healingItems", objectMapper.createArrayNode());
                ((ObjectNode) rootNode).set("stades", objectMapper.createArrayNode());
                saveDatabase();
            } else {
                String content = Files.readString(Paths.get(dbPath));
                if (content.trim().isEmpty()) {
                    // Initialize empty database
                    rootNode = objectMapper.createObjectNode();
                    ((ObjectNode) rootNode).set("pokemons", objectMapper.createArrayNode());
                    ((ObjectNode) rootNode).set("types", objectMapper.createArrayNode());
                    ((ObjectNode) rootNode).set("attaques", objectMapper.createArrayNode());
                    ((ObjectNode) rootNode).set("abilites", objectMapper.createArrayNode());
                    ((ObjectNode) rootNode).set("effectItems", objectMapper.createArrayNode());
                    ((ObjectNode) rootNode).set("healingItems", objectMapper.createArrayNode());
                    ((ObjectNode) rootNode).set("stades", objectMapper.createArrayNode());
                    saveDatabase();
                } else {
                    rootNode = objectMapper.readTree(content);
                    // Ensure all required arrays exist
                    ensureArrayExists("pokemons");
                    ensureArrayExists("types");
                    ensureArrayExists("attaques");
                    ensureArrayExists("abilites");
                    ensureArrayExists("effectItems");
                    ensureArrayExists("healingItems");
                    ensureArrayExists("stades");
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading database: " + e);
            e.printStackTrace();
        }
    }

    /**
     * Ensure an array exists in the root object
     */
    private void ensureArrayExists(String key) {
        if (!rootNode.has(key) || !rootNode.get(key).isArray()) {
            ((ObjectNode) rootNode).set(key, objectMapper.createArrayNode());
        }
    }

    /**
     * Save the entire database to JSON file
     */
    public synchronized void saveDatabase() {
        try {
            Files.write(Paths.get(dbPath), objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(rootNode));
        } catch (IOException e) {
            System.out.println("Error saving database: " + e);
            e.printStackTrace();
        }
    }

    /**
     * Get an array from the database (e.g., "Pokémon", "types")
     */
    public ArrayNode getArray(String arrayName) {
        if (rootNode.has(arrayName) && rootNode.get(arrayName).isArray()) {
            return (ArrayNode) rootNode.get(arrayName);
        }
        return objectMapper.createArrayNode();
    }

    /**
     * Get an object by ID from a named array
     */
    public JsonNode getObjectById(String arrayName, int id) {
        ArrayNode array = getArray(arrayName);
        for (JsonNode node : array) {
            if (node.has("id") && node.get("id").asInt() == id) {
                return node;
            }
        }
        return null;
    }

    /**
     * Add or update an object in a named array
     */
    public void saveObject(String arrayName, JsonNode object) {
        ArrayNode array = getArray(arrayName);
        
        if (object.has("id")) {
            int id = object.get("id").asInt();
            // Check if object already exists
            for (int i = 0; i < array.size(); i++) {
                if (array.get(i).has("id") && array.get(i).get("id").asInt() == id) {
                    // Update existing
                    array.set(i, object);
                    saveDatabase();
                    return;
                }
            }
        }
        
        // Add new
        array.add(object);
        saveDatabase();
    }

    /**
     * Delete an object by ID from a named array
     */
    public void deleteObject(String arrayName, int id) {
        ArrayNode array = getArray(arrayName);
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).has("id") && array.get(i).get("id").asInt() == id) {
                array.remove(i);
                saveDatabase();
                return;
            }
        }
    }

    /**
     * Get the ObjectMapper for serialization/deserialization
     */
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * Reload database from file
     */
    public void reload() {
        loadDatabase();
    }
}
