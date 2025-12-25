package com.ShofaKhafiy.MoneyManager.service;

import com.ShofaKhafiy.MoneyManager.dao.UserDAO;
import com.ShofaKhafiy.MoneyManager.model.User;

import java.util.Optional;

public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User register(String username, String password) {
        String id = java.util.UUID.randomUUID().toString();
        User newUser = new User(id, username, password, true);  // Default is active
        userDAO.save(newUser);
        return newUser;
    }

    public User login(String username, String password) {
        Optional<User> userOpt = userDAO.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;  // Return null if login fails
    }
}
