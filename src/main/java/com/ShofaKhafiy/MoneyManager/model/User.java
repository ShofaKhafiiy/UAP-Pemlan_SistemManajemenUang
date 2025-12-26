package com.ShofaKhafiy.MoneyManager.model;

public class User {
    private String username;
    private String password; // Dalam aplikasi nyata, disarankan menggunakan hashing

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
}