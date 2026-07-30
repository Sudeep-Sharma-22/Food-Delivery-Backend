package com.fooddelivery.model;

public class OrderItem {
    private int orderId;
    private int itemId;
    private int quantity;
    private double priceAtOrder;
    private double subtotal;

    public OrderItem() {}

    public OrderItem(int orderId, int itemId, int quantity, double priceAtOrder, double subtotal) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.priceAtOrder = priceAtOrder;
        this.subtotal = subtotal;
    }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPriceAtOrder() { return priceAtOrder; }
    public void setPriceAtOrder(double priceAtOrder) { this.priceAtOrder = priceAtOrder; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}
