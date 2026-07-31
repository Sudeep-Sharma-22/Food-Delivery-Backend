package com.fooddelivery.model;

import java.sql.Timestamp;

public class Restaurant {
    private int restaurantId;
    private int ownerId;
    private String name;
    private String cuisineType;
    private String location;
    private boolean isActive;
    private double avgRating;
    private Timestamp createdAt;

    public Restaurant() {}

    public Restaurant(int restaurantId, int ownerId, String name, String cuisineType, String location, boolean isActive, double avgRating, Timestamp createdAt) {
        this.restaurantId = restaurantId;
        this.ownerId = ownerId;
        this.name = name;
        this.cuisineType = cuisineType;
        this.location = location;
        this.isActive = isActive;
        this.avgRating = avgRating;
        this.createdAt = createdAt;
    }

    public int getRestaurantId() { return restaurantId; }
    public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }

    public int getOwnerId() { return ownerId; }
    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCuisineType() { return cuisineType; }
    public void setCuisineType(String cuisineType) { this.cuisineType = cuisineType; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public double getAvgRating() { return avgRating; }
    public void setAvgRating(double avgRating) { this.avgRating = avgRating; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return String.format("[%d] %s (%s) - Location: %s - Rating: %.1f/5.0",
                             restaurantId, name, cuisineType, location, avgRating);
    }
}
