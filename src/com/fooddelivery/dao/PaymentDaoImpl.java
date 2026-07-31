package com.fooddelivery.dao;

import com.fooddelivery.model.Payment;
import com.fooddelivery.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PaymentDaoImpl implements IPaymentDao {
    @Override
    public boolean processPayment(Payment payment) {
        String query = "INSERT INTO payments (order_id, amount, payment_method, payment_status) VALUES (?, ?, ?, ?)";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, payment.getOrderId());
            pstmt.setDouble(2, payment.getAmount());
            pstmt.setString(3, payment.getPaymentMethod());
            pstmt.setString(4, payment.getPaymentStatus());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error processing payment: " + e.getMessage());
            return false;
        }
    }
}
