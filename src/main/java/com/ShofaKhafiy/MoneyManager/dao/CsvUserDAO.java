package com.ShofaKhafiy.MoneyManager.dao;

import com.ShofaKhafiy.MoneyManager.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CsvUserDAO implements UserDAO {
    private String fileName;

    public CsvUserDAO(String fileName) {
        this.fileName = fileName;
        ensureFileExists(fileName); // Ensure the file exists when the object is created
    }

    @Override
    public void save(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            // Save user with id and isActive
            writer.write(user.getId() + "," + user.getUsername() + "," + user.getPassword() + "," + user.isActive());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        List<User> users = readAllUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    private List<User> readAllUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {  // We now expect 4 columns: id, username, password, and isActive
                    String id = data[0];  // User ID
                    String username = data[1];
                    String password = data[2];
                    boolean isActive = Boolean.parseBoolean(data[3]);  // Convert string to boolean
                    users.add(new User(id, username, password, isActive));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public List<User> findAll() {
        return readAllUsers();
    }

    @Override
    public boolean update(User user) {
        List<User> users = readAllUsers();
        boolean updated = false;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (User u : users) {
                if (u.getUsername().equals(user.getUsername())) {
                    writer.write(user.getId() + "," + user.getUsername() + "," + user.getPassword() + "," + user.isActive());
                    writer.newLine();
                    updated = true;
                } else {
                    writer.write(u.getId() + "," + u.getUsername() + "," + u.getPassword() + "," + u.isActive());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return updated;
    }

    @Override
    public boolean delete(String id) {
        List<User> users = readAllUsers();
        boolean deleted = false;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (User user : users) {
                if (!user.getId().equals(id)) {
                    writer.write(user.getId() + "," + user.getUsername() + "," + user.getPassword() + "," + user.isActive());
                    writer.newLine();
                } else {
                    deleted = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return deleted;
    }

    private void ensureFileExists(String fileName) {
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
