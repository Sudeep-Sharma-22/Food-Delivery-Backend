package com.fooddelivery.dao;

import com.fooddelivery.model.Address;
import com.fooddelivery.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddressDaoImpl implements IAddressDao {
    @Override
    public boolean insertAddress(Address address) {
        String query = "INSERT INTO addresses (user_id, address_line, city, state, pincode, is_default, label) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, address.getUserId());
            pstmt.setString(2, address.getAddressLine());
            pstmt.setString(3, address.getCity());
            pstmt.setString(4, address.getState());
            pstmt.setString(5, address.getPincode());
            pstmt.setBoolean(6, address.isDefault());
            pstmt.setString(7, address.getLabel());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting address: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Address> getAddressesByUserId(int userId) {
        List<Address> addresses = new ArrayList<>();
        String query = "SELECT * FROM addresses WHERE user_id = ?";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                addresses.add(new Address(
                    rs.getInt("address_id"),
                    rs.getInt("user_id"),
                    rs.getString("address_line"),
                    rs.getString("city"),
                    rs.getString("state"),
                    rs.getString("pincode"),
                    rs.getBoolean("is_default"),
                    rs.getString("label")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching addresses: " + e.getMessage());
        }
        return addresses;
    }
}
