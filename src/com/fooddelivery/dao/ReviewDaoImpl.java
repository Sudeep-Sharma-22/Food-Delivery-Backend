package com.fooddelivery.dao;

import com.fooddelivery.model.Review;
import com.fooddelivery.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReviewDaoImpl implements IReviewDao {
    @Override
    public boolean addReview(Review review) {
        String query = "INSERT INTO reviews (customer_id, restaurant_id, order_id, rating, comment) VALUES (?, ?, ?, ?, ?)";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, review.getCustomerId());
            pstmt.setInt(2, review.getRestaurantId());
            pstmt.setInt(3, review.getOrderId());
            pstmt.setInt(4, review.getRating());
            pstmt.setString(5, review.getComment());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding review: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Review> getReviewsByRestaurant(int restaurantId) {
        List<Review> reviews = new ArrayList<>();
        String query = "SELECT * FROM reviews WHERE restaurant_id = ?";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, restaurantId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                reviews.add(new Review(
                    rs.getInt("review_id"),
                    rs.getInt("customer_id"),
                    rs.getInt("restaurant_id"),
                    rs.getInt("order_id"),
                    rs.getInt("rating"),
                    rs.getString("comment"),
                    rs.getTimestamp("created_at")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching reviews: " + e.getMessage());
        }
        return reviews;
    }
}
