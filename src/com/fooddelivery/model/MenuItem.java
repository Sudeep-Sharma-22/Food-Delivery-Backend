package com.fooddelivery.model;

public class MenuItem {
    private int itemId;
    private int restaurantId;
    private String name;
    private String description;
    private double price;
    private boolean isVegetarian;
    private boolean isAvailable;
    private String category;

    public MenuItem() {}

    public MenuItem(int itemId, int restaurantId, String name, String description, double price, boolean isVegetarian, boolean isAvailable, String category) {
        this.itemId = itemId;
        this.restaurantId = restaurantId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.isVegetarian = isVegetarian;
        this.isAvailable = isAvailable;
        this.category = category;
    }

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public int getRestaurantId() { return restaurantId; }
    public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public boolean isVegetarian() { return isVegetarian; }
    public void setVegetarian(boolean vegetarian) { isVegetarian = vegetarian; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    @Override
    public String toString() {
        return String.format("[%d] %s (%.2f INR) - %s | %s",
                             itemId, name, price, isVegetarian ? "Veg" : "Non-Veg",
                             isAvailable ? "Available" : "Out of Stock");
    }
}
