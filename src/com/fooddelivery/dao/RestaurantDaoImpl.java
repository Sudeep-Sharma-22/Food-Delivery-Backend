package com.fooddelivery.dao;

import com.fooddelivery.model.Restaurant;
import com.fooddelivery.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RestaurantDaoImpl implements IRestaurantDao {
    @Override
    public List<Restaurant> getAllActiveRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();
        String query = "SELECT * FROM restaurants WHERE is_active = TRUE ORDER BY avg_rating DESC";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Restaurant r = new Restaurant(
                    rs.getInt("restaurant_id"),
                    rs.getInt("owner_id"),
                    rs.getString("name"),
                    rs.getString("cuisine_type"),
                    rs.getString("address"),
                    rs.getBoolean("is_active"),
                    rs.getDouble("avg_rating"),
                    rs.getTimestamp("created_at")
                );
                restaurants.add(r);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching restaurants: " + e.getMessage());
        }
        return restaurants;
    }

    @Override
    public Restaurant getRestaurantById(int restaurantId) {
        String query = "SELECT * FROM restaurants WHERE restaurant_id = ?";
        Restaurant r = null;
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, restaurantId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                r = new Restaurant(
                    rs.getInt("restaurant_id"),
                    rs.getInt("owner_id"),
                    rs.getString("name"),
                    rs.getString("cuisine_type"),
                    rs.getString("address"),
                    rs.getBoolean("is_active"),
                    rs.getDouble("avg_rating"),
                    rs.getTimestamp("created_at")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error fetching restaurant: " + e.getMessage());
        }
        return r;
    }

    @Override
    public boolean addRestaurant(Restaurant restaurant) {
        String query = "INSERT INTO restaurants (owner_id, name, cuisine_type, address, city) VALUES (?, ?, ?, ?, ?)";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, restaurant.getOwnerId());
            pstmt.setString(2, restaurant.getName());
            pstmt.setString(3, restaurant.getCuisineType());
            pstmt.setString(4, restaurant.getLocation());
            pstmt.setString(5, "Demo City");
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding restaurant: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Restaurant> getRestaurantsByOwner(int ownerId) {
        List<Restaurant> restaurants = new ArrayList<>();
        String query = "SELECT * FROM restaurants WHERE owner_id = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, ownerId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                restaurants.add(new Restaurant(
                    rs.getInt("restaurant_id"),
                    rs.getInt("owner_id"),
                    rs.getString("name"),
                    rs.getString("cuisine_type"),
                    rs.getString("address"),
                    rs.getBoolean("is_active"),
                    rs.getDouble("avg_rating"),
                    rs.getTimestamp("created_at")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching owner's restaurants: " + e.getMessage());
        }
        return restaurants;
    }
}
