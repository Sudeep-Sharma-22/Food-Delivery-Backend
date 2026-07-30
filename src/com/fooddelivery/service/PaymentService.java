package com.fooddelivery.service;

import com.fooddelivery.dao.IPaymentDao;
import com.fooddelivery.dao.PaymentDaoImpl;
import com.fooddelivery.model.Payment;

public class PaymentService {
    private IPaymentDao paymentDao;

    public PaymentService() {
        this.paymentDao = new PaymentDaoImpl();
    }

    public boolean makePayment(int orderId, double amount, String method) {
        if (amount <= 0) {
            System.err.println("Validation Error: Invalid amount.");
            return false;
        }

        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(amount);
        payment.setPaymentMethod(method.toUpperCase());
        payment.setPaymentStatus("COMPLETED"); // Simulated successful payment

        return paymentDao.processPayment(payment);
    }
}
