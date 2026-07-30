package com.fooddelivery.model;

import java.sql.Timestamp;

public class Payment {
    private int paymentId;
    private int orderId;
    private double amount;
    private String paymentMethod;
    private String paymentStatus;
    private Timestamp transactionTime;

    public Payment() {}

    public Payment(int paymentId, int orderId, double amount, String paymentMethod, String paymentStatus, Timestamp transactionTime) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.transactionTime = transactionTime;
    }

    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public Timestamp getTransactionTime() { return transactionTime; }
    public void setTransactionTime(Timestamp transactionTime) { this.transactionTime = transactionTime; }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", orderId=" + orderId +
                ", amount=" + amount +
                ", method='" + paymentMethod + '\'' +
                ", status='" + paymentStatus + '\'' +
                '}';
    }
}
