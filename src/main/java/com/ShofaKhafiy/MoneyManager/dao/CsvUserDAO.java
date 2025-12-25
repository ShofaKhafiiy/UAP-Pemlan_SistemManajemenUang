package com.ShofaKhafiy.MoneyManager.dao;

import com.ShofaKhafiy.MoneyManager.model.User;
import com.ShofaKhafiy.MoneyManager.util.CsvUtil;

import java.io.*;
import java.util.*;

public class CsvUserDAO implements UserDAO {

    private final String filePath;

    public CsvUserDAO(String filePath) {
        this.filePath = filePath;
        CsvUtil.ensureFileExists(filePath);  // Ensure file exists
    }

    @Override
    public void save(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(CsvUtil.userToCsv(user));
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Error saving user.", e);
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return findAll().stream().filter(u -> u.getUsername().equals(username)).findFirst();
    }

    @Override
    public Optional<User> findById(String id) {
        return findAll().stream().filter(u -> u.getId().equals(id)).findFirst();
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    users.add(CsvUtil.userFromCsv(line));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading users.", e);
        }
        return users;
    }

    @Override
    public boolean update(User user) {
        List<User> all = findAll();
        boolean updated = false;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (User u : all) {
                if (u.getId().equals(user.getId())) {
                    writer.write(CsvUtil.userToCsv(user));
                    updated = true;
                } else {
                    writer.write(CsvUtil.userToCsv(u));
                }
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error updating user.", e);
        }
        return updated;
    }

    @Override
    public boolean delete(String id) {
        List<User> all = findAll();
        boolean deleted = false;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (User u : all) {
                if (u.getId().equals(id)) {
                    deleted = true;
                    continue;
                }
                writer.write(CsvUtil.userToCsv(u));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error deleting user.", e);
        }
        return deleted;
    }
}
