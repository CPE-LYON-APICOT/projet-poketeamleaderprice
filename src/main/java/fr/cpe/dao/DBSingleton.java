package fr.cpe.dao;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DBSingleton {

    private static DBSingleton instance;
    private java.sql.Connection connection;

    private DBSingleton() {
        try {
            String url = "jdbc:sqlite:" + System.getenv("DB_PATH");
            connection = DriverManager.getConnection(url);
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