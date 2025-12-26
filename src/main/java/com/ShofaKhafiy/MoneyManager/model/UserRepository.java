package com.ShofaKhafiy.MoneyManager.model;

import com.ShofaKhafiy.MoneyManager.model.User;

public interface UserRepository {
    void save(User user);
    User findByUsername(String username);
    boolean exists(String username);
}