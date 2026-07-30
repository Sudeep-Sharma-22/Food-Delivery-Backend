package com.fooddelivery.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/food_delivery";
    private static final String USER = "root";
    private static final String PASSWORD = "Sudeep@220604"; // Update this with your actual MySQL password

    // The single instance of Connection
    private static Connection connection = null;

    // Private constructor prevents instantiation from other classes
    private DatabaseConnection() {}

    // Public method to get the connection instance
    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Load the MySQL JDBC driver
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                // Establish the connection
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Database connection established successfully.");
            } catch (ClassNotFoundException e) {
                System.err.println("MySQL JDBC Driver not found. Add the jar to your classpath: " + e.getMessage());
            } catch (SQLException e) {
                System.err.println("Failed to connect to the database: " + e.getMessage());
            }
        }
        return connection;
    }
}
