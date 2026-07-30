package com.fooddelivery.dao;

import com.fooddelivery.model.Payment;

public interface IPaymentDao {
    boolean processPayment(Payment payment);
}
