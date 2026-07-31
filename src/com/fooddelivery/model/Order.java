package com.fooddelivery.model;

import java.sql.Timestamp;

public class Order {
    private int orderId;
    private int customerId;
    private int restaurantId;
    private int deliveryAddressId;
    private int couponId;
    private double totalAmount;
    private double discountAmount;
    private double finalAmount;
    private String status;
    private Timestamp orderTime;

    public Order() {}

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getRestaurantId() { return restaurantId; }
    public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }

    public int getDeliveryAddressId() { return deliveryAddressId; }
    public void setDeliveryAddressId(int deliveryAddressId) { this.deliveryAddressId = deliveryAddressId; }

    public int getCouponId() { return couponId; }
    public void setCouponId(int couponId) { this.couponId = couponId; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public double getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(double discountAmount) { this.discountAmount = discountAmount; }

    public double getFinalAmount() { return finalAmount; }
    public void setFinalAmount(double finalAmount) { this.finalAmount = finalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getOrderTime() { return orderTime; }
    public void setOrderTime(Timestamp orderTime) { this.orderTime = orderTime; }

    @Override
    public String toString() {
        return String.format("Order #%d | Status: %s | Final Amount: %.2f INR",
                             orderId, status, finalAmount);
    }
}
