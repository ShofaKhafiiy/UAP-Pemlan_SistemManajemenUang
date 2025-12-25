package com.ShofaKhafiy.MoneyManager.model;  // Sesuaikan dengan folder tempat file berada

public class User {
    private String id;
    private String username;
    private String password;
    private boolean isActive;

    public User(String id, String username, String password, boolean isActive) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.isActive = isActive;
    }

    // Getters and Setters
    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public boolean isActive() { return isActive; }
}
