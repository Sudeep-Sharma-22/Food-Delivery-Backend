package com.fooddelivery.dao;

import com.fooddelivery.model.MenuItem;
import java.util.List;

public interface IMenuItemDao {
    List<MenuItem> getMenuByRestaurant(int restaurantId);
    boolean addMenuItem(MenuItem item);
}
