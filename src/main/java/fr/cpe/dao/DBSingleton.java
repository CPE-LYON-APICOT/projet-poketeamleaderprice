package fr.cpe.db;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DBSingleton {

    private static DBSingleton instance;
    private java.sql.Connection connection;

    private DBSingleton() {
        try {
            String url = "jdbc:sqlite:c:" + System.getenv("DB_PATH");
            String username = System.getenv("DB_USERNAME");
            String password = System.getenv("DB_PASSWORD");

            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DBSingleton getInstance() {
        if (instance == null) {
            instance = new DBSingleton();
        }
        return instance;
    }

    public java.sql.Connection getConnection() {
        return connection;
    }
}