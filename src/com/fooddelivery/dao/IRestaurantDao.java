package com.fooddelivery.dao;

import com.fooddelivery.model.Restaurant;
import java.util.List;

public interface IRestaurantDao {
    List<Restaurant> getAllActiveRestaurants();
    Restaurant getRestaurantById(int restaurantId);
    boolean addRestaurant(Restaurant restaurant);
    List<Restaurant> getRestaurantsByOwner(int ownerId);
}
