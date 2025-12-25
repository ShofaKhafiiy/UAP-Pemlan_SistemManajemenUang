package com.ShofaKhafiy.MoneyManager.model;  // Sesuaikan dengan folder tempat file berada

import java.time.LocalDate;

public class Transaction {
    private String id;
    private String type; // "income" or "expense"
    private double amount;
    private String description;
    private double balanceAfter;
    private LocalDate date;

    public Transaction(String id, String type, double amount, String description, double balanceAfter, LocalDate date) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.balanceAfter = balanceAfter;
        this.date = date;
    }

    // Getters and Setters
    public String getId() { return id; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
    public double getBalanceAfter() { return balanceAfter; }
    public LocalDate getDate() { return date; }
}
