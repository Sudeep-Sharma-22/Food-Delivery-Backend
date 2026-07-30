package com.fooddelivery.dao;

import com.fooddelivery.model.Order;
import com.fooddelivery.model.OrderItem;
import com.fooddelivery.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoImpl implements IOrderDao {

    @Override
    public boolean placeOrder(Order order, List<OrderItem> items) {
        Connection conn = DatabaseConnection.getConnection();
        boolean isSuccess = false;

        String insertOrderSql = "INSERT INTO orders (customer_id, restaurant_id, delivery_address_id, total_amount, discount_amount, final_amount, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String insertItemSql = "INSERT INTO order_items (order_id, item_id, quantity, price_at_order, subtotal) VALUES (?, ?, ?, ?, ?)";

        try {
            // 1. START TRANSACTION (Disable Auto-Commit)
            conn.setAutoCommit(false);

            // 2. Insert the main order
            // We use RETURN_GENERATED_KEYS so we can get the auto-incremented order_id
            try (PreparedStatement orderStmt = conn.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS)) {
                orderStmt.setInt(1, order.getCustomerId());
                orderStmt.setInt(2, order.getRestaurantId());
                orderStmt.setInt(3, order.getDeliveryAddressId());
                orderStmt.setDouble(4, order.getTotalAmount());
                orderStmt.setDouble(5, order.getDiscountAmount());
                orderStmt.setDouble(6, order.getFinalAmount());
                orderStmt.setString(7, order.getStatus());

                int rowsAffected = orderStmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Creating order failed, no rows affected.");
                }

                // 3. Retrieve the generated order_id
                int generatedOrderId;
                try (ResultSet generatedKeys = orderStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedOrderId = generatedKeys.getInt(1);
                        order.setOrderId(generatedOrderId);
                    } else {
                        throw new SQLException("Creating order failed, no ID obtained.");
                    }
                }

                // 4. Insert each order item linked to the new order_id
                try (PreparedStatement itemStmt = conn.prepareStatement(insertItemSql)) {
                    for (OrderItem item : items) {
                        itemStmt.setInt(1, generatedOrderId);
                        itemStmt.setInt(2, item.getItemId());
                        itemStmt.setInt(3, item.getQuantity());
                        itemStmt.setDouble(4, item.getPriceAtOrder());
                        itemStmt.setDouble(5, item.getSubtotal());
                        
                        // Add to batch for better performance on multiple inserts
                        itemStmt.addBatch(); 
                    }
                    // Execute all item inserts at once
                    itemStmt.executeBatch();
                }
            }

            // 5. COMMIT TRANSACTION (If we reach here, everything succeeded)
            conn.commit();
            isSuccess = true;

        } catch (SQLException e) {
            System.err.println("Transaction Failed! Rolling back. Error: " + e.getMessage());
            try {
                // 6. ROLLBACK TRANSACTION (Undo partial inserts)
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Rollback failed: " + ex.getMessage());
            }
        } finally {
            try {
                // 7. Reset auto-commit so the connection can be used normally later
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                System.err.println("Failed to reset auto-commit: " + e.getMessage());
            }
        }

        return isSuccess;
    }

    @Override
    public boolean updateOrderStatus(int orderId, String status) {
        String query = "UPDATE orders SET status = ? WHERE order_id = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, orderId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating order status: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Order> getOrdersByCustomer(int customerId) {
        return getOrders("SELECT * FROM orders WHERE customer_id = ? ORDER BY order_time DESC", customerId);
    }

    @Override
    public List<Order> getOrdersByRestaurant(int restaurantId) {
        return getOrders("SELECT * FROM orders WHERE restaurant_id = ? ORDER BY order_time DESC", restaurantId);
    }

    @Override
    public List<Order> getOrdersByStatus(String status) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE status = ? ORDER BY order_time ASC";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                orders.add(mapRowToOrder(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching orders: " + e.getMessage());
        }
        return orders;
    }

    @Override
    public boolean assignDeliveryPartner(int orderId, int partnerId) {
        String query = "UPDATE orders SET delivery_partner_id = ?, status = 'OUT_FOR_DELIVERY' WHERE order_id = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, partnerId);
            pstmt.setInt(2, orderId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error assigning partner: " + e.getMessage());
            return false;
        }
    }

    private List<Order> getOrders(String query, int id) {
        List<Order> orders = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                orders.add(mapRowToOrder(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching orders: " + e.getMessage());
        }
        return orders;
    }

    private Order mapRowToOrder(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setOrderId(rs.getInt("order_id"));
        o.setCustomerId(rs.getInt("customer_id"));
        o.setRestaurantId(rs.getInt("restaurant_id"));
        o.setDeliveryAddressId(rs.getInt("delivery_address_id"));
        o.setTotalAmount(rs.getDouble("total_amount"));
        o.setFinalAmount(rs.getDouble("final_amount"));
        o.setStatus(rs.getString("status"));
        return o;
    }

    @Override
    public List<Order> getOrdersByDeliveryPartner(int partnerId) {
        return getOrders("SELECT * FROM orders WHERE delivery_partner_id = ? ORDER BY order_time DESC", partnerId);
    }
}
