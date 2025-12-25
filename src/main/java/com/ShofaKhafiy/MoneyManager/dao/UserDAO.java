package com.ShofaKhafiy.MoneyManager.dao;

import com.ShofaKhafiy.MoneyManager.model.User;

import java.util.Optional;
import java.util.List;

public interface UserDAO {
    void save(User user);
    Optional<User> findByUsername(String username);
    Optional<User> findById(String id);
    List<User> findAll();
    boolean update(User user);
    boolean delete(String id);
}
