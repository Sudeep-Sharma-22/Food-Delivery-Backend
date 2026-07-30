package com.fooddelivery.dao;

import com.fooddelivery.model.User;
import java.util.List;

public interface IUserDao {
    // Defines WHAT operations we can do with the Users table
    boolean insertUser(User user);
    User getUserById(int userId);
    User getUserByEmail(String email);
    List<User> getAllCustomers();
}
