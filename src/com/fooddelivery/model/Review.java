package com.fooddelivery.model;

import java.sql.Timestamp;

public class Review {
    private int reviewId;
    private int customerId;
    private int restaurantId;
    private int orderId;
    private int rating;
    private String comment;
    private Timestamp createdAt;

    public Review() {}

    public Review(int reviewId, int customerId, int restaurantId, int orderId, int rating, String comment, Timestamp createdAt) {
        this.reviewId = reviewId;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.orderId = orderId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public int getReviewId() { return reviewId; }
    public void setReviewId(int reviewId) { this.reviewId = reviewId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getRestaurantId() { return restaurantId; }
    public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Review{" +
                "rating=" + rating +
                " Stars, comment='" + comment + '\'' +
                '}';
    }
}
