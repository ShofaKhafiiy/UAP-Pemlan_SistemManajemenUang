package com.ShofaKhafiy.MoneyManager.dao;

import com.ShofaKhafiy.MoneyManager.model.User;
import java.util.List;
import java.util.Optional;

public interface UserDAO {
    void save(User user);
    Optional<User> findByUsername(String username);
    List<User> findAll();
    boolean update(User user);
    boolean delete(String id);
}
