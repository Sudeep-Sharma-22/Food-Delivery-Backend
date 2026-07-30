package com.fooddelivery.dao;

import com.fooddelivery.model.MenuItem;
import com.fooddelivery.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuItemDaoImpl implements IMenuItemDao {

    @Override
    public List<MenuItem> getMenuByRestaurant(int restaurantId) {
        List<MenuItem> menu = new ArrayList<>();
        String query = "SELECT * FROM menu_items WHERE restaurant_id = ? AND is_available = TRUE ORDER BY category";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, restaurantId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                MenuItem item = new MenuItem(
                    rs.getInt("item_id"),
                    rs.getInt("restaurant_id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getBoolean("is_vegetarian"),
                    rs.getBoolean("is_available"),
                    rs.getString("category")
                );
                menu.add(item);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching menu items: " + e.getMessage());
        }
        return menu;
    }

    @Override
    public boolean addMenuItem(MenuItem item) {
        String query = "INSERT INTO menu_items (restaurant_id, name, description, price, category, is_vegetarian) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, item.getRestaurantId());
            pstmt.setString(2, item.getName());
            pstmt.setString(3, item.getDescription());
            pstmt.setDouble(4, item.getPrice());
            pstmt.setString(5, item.getCategory());
            pstmt.setBoolean(6, item.isVegetarian());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding menu item: " + e.getMessage());
            return false;
        }
    }
}
