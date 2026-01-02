package com.smartecommerce.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

/**
 * DatabaseConnection manages the database connection using JDBC
 * Implements Singleton pattern to ensure single connection instance
 */
public class DatabaseConfig {
    private static final ConfigManager CONFIG = ConfigManager.getInstance();
    private static final String URL = CONFIG.buildJdbcUrl();
    private static final String USER = CONFIG.getString("database.username", "root");
    private static final String PASSWORD = CONFIG.getString("database.password", "");

    // Private constructor to prevent instantiation
    private DatabaseConfig() {
    }

    /**
     * Get database connection using Singleton pattern
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Load driver based on configured database type
            Class.forName(CONFIG.getJdbcDriverClass());
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found!" + e.getMessage());
            e.printStackTrace();
            throw new SQLException("Driver not found", e);
        } catch (SQLException e) {
            System.err.println("Connection failed! Check database credentials and ensure the database is running.");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Convenience for building a connection with additional JDBC properties when supported.
     */
    public static Connection getConnectionWithProps() throws SQLException {
        try {
            Class.forName(CONFIG.getJdbcDriverClass());
            Properties props = new Properties();
            props.put("user", USER);
            props.put("password", PASSWORD);
            for (Map.Entry<String, String> entry : CONFIG.getAdditionalJdbcProperties().entrySet()) {
                props.put(entry.getKey(), entry.getValue());
            }
            return DriverManager.getConnection(URL, props);
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found!" + e.getMessage());
            e.printStackTrace();
            throw new SQLException("Driver not found", e);
        } catch (SQLException e) {
            System.err.println("Connection failed! Check database credentials and ensure the database is running.");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Close the database connection
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing database connection!");
                e.printStackTrace();
            }
        }
    }

    /**
     * Test database connection
     * @return true if connection is successful
     */
    public static boolean testConnection() {
        try {
            try (Connection conn = getConnection()) {
                return conn != null && !conn.isClosed();
            }
        } catch (SQLException e) {
            System.err.println("Connection test failed!");
            e.printStackTrace();
            return false;
        }
    }
}
