package fr.cpe.dao;

/**
 * Singleton pattern for database access
 * Now uses JSONManager for JSON-based database operations
 */
public class DBSingleton {

    private static DBSingleton instance;
    private JSONManager jsonManager;

    private DBSingleton() {
        this.jsonManager = JSONManager.getInstance();
    }

    public static DBSingleton getInstance() {
        if (instance == null) {
            instance = new DBSingleton();
        }
        return instance;
    }

    /**
     * Get the JSONManager for database operations
     */
    public JSONManager getJSONManager() {
        return jsonManager;
    }

    /**
     * Legacy method for compatibility - returns JSONManager
     * @deprecated Use getJSONManager() instead
     */
    @Deprecated
    public Object getConnection() {
        return jsonManager;
    }
}