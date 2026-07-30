package com.fooddelivery.dao;

import com.fooddelivery.model.User;
import com.fooddelivery.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements IUserDao {

    @Override
    public boolean insertUser(User user) {
        // 1. SQL query with '?' placeholders (Parameterization)
        String query = "INSERT INTO users (name, email, phone, password, role) VALUES (?, ?, ?, ?, ?)";
        
        // 2. Get the Singleton connection
        Connection conn = DatabaseConnection.getConnection();
        
        // 3. Use try-with-resources ONLY for the PreparedStatement, NOT the Connection
        // If we close the Connection, it kills our Singleton and crashes the app!
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            // 4. Bind the data to the placeholders
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPhone());
            pstmt.setString(4, user.getPasswordHash());
            pstmt.setString(5, user.getRole());
            
            // 5. Execute DML (Insert/Update/Delete) and get rows affected
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error inserting user: " + e.getMessage());
            return false;
        }
    }

    @Override
    public User getUserById(int userId) {
        String query = "SELECT * FROM users WHERE user_id = ?";
        User user = null;
        Connection conn = DatabaseConnection.getConnection();
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setInt(1, userId);
            
            // Execute DQL (Select) to get the ResultSet
            ResultSet rs = pstmt.executeQuery();
            
            // Move cursor to the first row (if it exists)
            if (rs.next()) {
                // Map the ResultSet row back into our Java Object
                user = new User(
                    rs.getInt("user_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getTimestamp("created_at")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user: " + e.getMessage());
        }
        
        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        User user = null;
        Connection conn = DatabaseConnection.getConnection();
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                user = new User(
                    rs.getInt("user_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getTimestamp("created_at")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user by email: " + e.getMessage());
        }
        
        return user;
    }

    @Override
    public List<User> getAllCustomers() {
        List<User> customers = new ArrayList<>();
        String query = "SELECT * FROM users WHERE role = 'CUSTOMER'";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            
            // while() loop because there are multiple rows
            while (rs.next()) {
                User u = new User(
                    rs.getInt("user_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getTimestamp("created_at")
                );
                customers.add(u);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching customers: " + e.getMessage());
        }
        return customers;
    }
}
