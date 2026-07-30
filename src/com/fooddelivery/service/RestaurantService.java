package com.fooddelivery.service;

import com.fooddelivery.dao.IRestaurantDao;
import com.fooddelivery.dao.RestaurantDaoImpl;
import com.fooddelivery.model.Restaurant;

import java.util.List;

public class RestaurantService {
    private IRestaurantDao restaurantDao;

    public RestaurantService() {
        this.restaurantDao = new RestaurantDaoImpl();
    }

    public void displayOpenRestaurants() {
        List<Restaurant> restaurants = restaurantDao.getAllActiveRestaurants();
        System.out.println("--- OPEN RESTAURANTS ---");
        if (restaurants.isEmpty()) {
            System.out.println("No restaurants are currently open.");
            return;
        }
        for (Restaurant r : restaurants) {
            System.out.println(r.toString());
        }
    }

    public boolean addRestaurant(int ownerId, String name, String cuisine, String address) {
        if (name == null || name.isEmpty()) return false;
        Restaurant r = new Restaurant();
        r.setOwnerId(ownerId);
        r.setName(name);
        r.setCuisineType(cuisine);
        r.setLocation(address);
        return restaurantDao.addRestaurant(r);
    }

    public List<Restaurant> getRestaurantsByOwner(int ownerId) {
        return restaurantDao.getRestaurantsByOwner(ownerId);
    }

    public Restaurant getRestaurantById(int restaurantId) {
        return restaurantDao.getRestaurantById(restaurantId);
    }
}
