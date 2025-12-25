package com.ShofaKhafiy.MoneyManager.service;

import com.ShofaKhafiy.MoneyManager.dao.UserDAO;
import com.ShofaKhafiy.MoneyManager.model.User;

import java.util.Optional;
import java.util.UUID;

public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    // ================= REGISTER =================

    public User register(String username, String password) {
        // Generate a unique ID for the user
        String id = UUID.randomUUID().toString();

        // Create a new user object
        User newUser = new User(id, username, password, true);  // Active by default

        // Save the user to the data store (CSV)
        userDAO.save(newUser);

        return newUser;
    }

    // ================= LOGIN =================

    public User login(String username, String password) {
        // Find the user by username
        Optional<User> userOpt = userDAO.findByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Check if password matches
            if (user.getPassword().equals(password)) {
                return user;  // Return the logged-in user if credentials match
            }
        }
        // Return null if login fails
        return null;
    }

    // ================= UPDATE =================

    public boolean updateUser(User user) {
        return userDAO.update(user);  // Update user in the data store
    }

    // ================= DELETE =================

    public boolean deleteUser(String userId) {
        return userDAO.delete(userId);  // Delete the user from the data store
    }
}
