package com.fooddelivery.dao;

import com.fooddelivery.model.Order;
import com.fooddelivery.model.OrderItem;
import java.util.List;

public interface IOrderDao {
    boolean placeOrder(Order order, List<OrderItem> items);
    boolean updateOrderStatus(int orderId, String status);
    List<Order> getOrdersByCustomer(int customerId);
    List<Order> getOrdersByRestaurant(int restaurantId);
    List<Order> getOrdersByStatus(String status);
    boolean assignDeliveryPartner(int orderId, int partnerId);
    List<Order> getOrdersByDeliveryPartner(int partnerId);
}
