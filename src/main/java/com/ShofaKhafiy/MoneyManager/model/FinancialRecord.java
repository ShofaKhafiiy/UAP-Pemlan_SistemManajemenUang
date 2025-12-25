package com.ShofaKhafiy.MoneyManager.model;

import java.time.LocalDate;

public class FinancialRecord {
    private String id;
    private String type; // "income" or "expense"
    private double amount;
    private String description;
    private LocalDate date;

    public FinancialRecord(String id, String type, double amount, String description, LocalDate date) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    // Getters and Setters
    public String getId() { return id; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
    public LocalDate getDate() { return date; }
}
