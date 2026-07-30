package com.fooddelivery.service;

import com.fooddelivery.dao.IOrderDao;
import com.fooddelivery.dao.OrderDaoImpl;
import com.fooddelivery.model.Order;
import com.fooddelivery.model.OrderItem;

import java.util.List;

public class OrderService {
    private IOrderDao orderDao;

    public OrderService() {
        this.orderDao = new OrderDaoImpl();
    }

    public boolean placeOrder(Order order, List<OrderItem> items) {
        // Business Validation
        if (order.getCustomerId() <= 0 || order.getRestaurantId() <= 0) {
            System.err.println("Validation Error: Invalid Customer or Restaurant ID.");
            return false;
        }
        if (items == null || items.isEmpty()) {
            System.err.println("Validation Error: Order must contain at least one item.");
            return false;
        }

        // Calculate total amount from items to ensure no tampering
        double calculatedTotal = 0;
        for (OrderItem item : items) {
            if (item.getQuantity() <= 0) {
                System.err.println("Validation Error: Quantity must be at least 1.");
                return false;
            }
            calculatedTotal += item.getSubtotal();
        }

        // Apply calculated total to order (overriding whatever was passed in)
        order.setTotalAmount(calculatedTotal);
        order.setFinalAmount(calculatedTotal - order.getDiscountAmount());
        order.setStatus("PLACED");

        return orderDao.placeOrder(order, items);
    }

    public boolean updateOrderStatus(int orderId, String newStatus) {
        return orderDao.updateOrderStatus(orderId, newStatus);
    }

    public List<Order> getCustomerOrders(int customerId) {
        return orderDao.getOrdersByCustomer(customerId);
    }

    public List<Order> getRestaurantOrders(int restaurantId) {
        return orderDao.getOrdersByRestaurant(restaurantId);
    }

    public List<Order> getReadyOrders() {
        return orderDao.getOrdersByStatus("READY");
    }

    public boolean acceptDelivery(int orderId, int partnerId) {
        return orderDao.assignDeliveryPartner(orderId, partnerId);
    }

    public List<Order> getDeliveryPartnerOrders(int partnerId) {
        return orderDao.getOrdersByDeliveryPartner(partnerId);
    }
}
