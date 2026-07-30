package com.fooddelivery.service;

import com.fooddelivery.dao.IUserDao;
import com.fooddelivery.dao.UserDaoImpl;
import com.fooddelivery.model.User;

public class UserService {
    // The service depends on the DAO interface, NOT the database connection directly.
    private IUserDao userDao;

    public UserService() {
        // In a real app, this would be injected via Dependency Injection (e.g. Spring Framework)
        this.userDao = new UserDaoImpl();
    }

    /**
     * Business Logic: Register a new user.
     * We don't just blindly insert them into the DB. We first validate their data.
     */
    public boolean registerUser(String name, String email, String phone, String password, String role) {
        // 1. Business Validation (No empty fields)
        if (name == null || email == null || password == null || name.trim().isEmpty()) {
            System.err.println("Validation Error: Name, email, and password cannot be empty.");
            return false;
        }
        
        // 2. Business Validation (Email must contain '@')
        if (!email.contains("@")) {
            System.err.println("Validation Error: Invalid email format.");
            return false;
        }

        // 4. Create the User POJO
        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPhone(phone);
        newUser.setPasswordHash(password);
        newUser.setRole(role);

        // 5. Delegate to DAO to actually save to DB
        return userDao.insertUser(newUser);
    }
    
    /**
     * Business Logic: Get User profile by ID
     */
    public User getUserProfile(int userId) {
        // Simple validation
        if (userId <= 0) {
            System.err.println("Validation Error: Invalid User ID.");
            return null;
        }
        return userDao.getUserById(userId);
    }

    /**
     * Business Logic: Login User
     * Authenticates by checking email and matching password hashes.
     */
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
