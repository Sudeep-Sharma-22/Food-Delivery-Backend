package com.fooddelivery.dao;

import com.fooddelivery.model.User;
import java.util.List;

public interface IUserDao {
    boolean insertUser(User user);
    User getUserById(int userId);
    User getUserByEmail(String email);
    List<User> getAllCustomers();
}
