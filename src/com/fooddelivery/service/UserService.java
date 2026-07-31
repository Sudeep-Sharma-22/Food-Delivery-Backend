package com.fooddelivery.service;

import com.fooddelivery.dao.IUserDao;
import com.fooddelivery.dao.UserDaoImpl;
import com.fooddelivery.model.User;

public class UserService {
    private IUserDao userDao;

    public UserService() {
        this.userDao = new UserDaoImpl();
    }

    public boolean registerUser(String name, String email, String phone, String password, String role) {
        if (name == null || email == null || password == null || name.trim().isEmpty()) {
            System.err.println("Validation Error: Name, email, and password cannot be empty.");
            return false;
        }

        if (!email.contains("@")) {
            System.err.println("Validation Error: Invalid email format.");
            return false;
        }

        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPhone(phone);
        newUser.setPasswordHash(password);
        newUser.setRole(role);

        return userDao.insertUser(newUser);
    }

    public User getUserProfile(int userId) {
        if (userId <= 0) {
            System.err.println("Validation Error: Invalid User ID.");
            return null;
        }
        return userDao.getUserById(userId);
    }

    public User loginUser(String email, String rawPassword) {
        if (email == null || rawPassword == null) {
            System.err.println("Validation Error: Email and password cannot be empty.");
            return null;
        }

        User user = userDao.getUserByEmail(email);
        if (user == null) {
            System.err.println("Login Failed: User not found.");
            return null;
        }

        if (!user.getPasswordHash().equals(rawPassword)) {
            System.err.println("Login Failed: Incorrect password.");
            return null;
        }

        return user;
    }
}
