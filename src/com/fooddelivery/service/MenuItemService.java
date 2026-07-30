package com.fooddelivery.service;

import com.fooddelivery.dao.IMenuItemDao;
import com.fooddelivery.dao.MenuItemDaoImpl;
import com.fooddelivery.model.MenuItem;

import java.util.List;

public class MenuItemService {
    private IMenuItemDao menuItemDao;

    public MenuItemService() {
        this.menuItemDao = new MenuItemDaoImpl();
    }

    public void displayMenu(int restaurantId) {
        List<MenuItem> menu = menuItemDao.getMenuByRestaurant(restaurantId);
        System.out.println("--- MENU FOR RESTAURANT " + restaurantId + " ---");
        if (menu.isEmpty()) {
            System.out.println("No items available currently.");
            return;
        }
        for (MenuItem item : menu) {
            System.out.println(item.toString());
        }
    }

    public boolean addMenuItem(int restId, String name, String desc, double price, String cat, boolean isVeg) {
        if (price <= 0 || name == null || name.isEmpty()) return false;
        MenuItem item = new MenuItem();
        item.setRestaurantId(restId);
        item.setName(name);
        item.setDescription(desc);
        item.setPrice(price);
        item.setCategory(cat);
        item.setVegetarian(isVeg);
        return menuItemDao.addMenuItem(item);
    }

    public List<MenuItem> getMenu(int restaurantId) {
        return menuItemDao.getMenuByRestaurant(restaurantId);
    }
}
