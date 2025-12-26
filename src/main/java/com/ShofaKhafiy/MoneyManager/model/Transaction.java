package com.ShofaKhafiy.MoneyManager.model;
import java.time.LocalDate;

public class Transaction {
    private String id;
    private String owner; // <--- Akun pemilik
    private TransactionType type;
    private double amount;
    private Category category;
    private String description;
    private double balanceAfter;
    private boolean isDeleted;
    private LocalDate createdAt;

    public static class Builder {
        private String id, owner, description;
        private TransactionType type;
        private double amount, balanceAfter;
        private Category category;
        private boolean isDeleted;
        private LocalDate createdAt;

        public Builder(String id, TransactionType type) {
            this.id = id;
            this.type = type;
            this.createdAt = LocalDate.now();
        }
        public Builder owner(String owner) { this.owner = owner; return this; }
        public Builder amount(double amount) { this.amount = amount; return this; }
        public Builder category(Category category) { this.category = category; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder balanceAfter(double balanceAfter) { this.balanceAfter = balanceAfter; return this; }
        public Builder deleted(boolean isDeleted) { this.isDeleted = isDeleted; return this; }
        public Builder setCreatedAt(LocalDate date) { this.createdAt = date; return this; }

        public Transaction build() {
            Transaction t = new Transaction();
            t.id = id; t.owner = owner; t.type = type;
            t.amount = amount; t.category = category;
            t.description = description; t.balanceAfter = balanceAfter;
            t.isDeleted = isDeleted; t.createdAt = createdAt;
            return t;
        }
    }
    // TAMBAHKAN SETTER INI DI Transaction.java
    public void setAmount(double amount) { this.amount = amount; }
    public void setCategory(Category category) { this.category = category; }
    public void setDescription(String description) { this.description = description; }

    // Getter yang sudah ada
    public String getOwner() { return owner; }
    public String getId() { return id; }
    public TransactionType getType() { return type; }
    public double getAmount() { return amount; }
    public Category getCategory() { return category; }
    public String getDescription() { return description; }
    public double getBalanceAfter() { return balanceAfter; }
    public boolean isDeleted() { return isDeleted; }
    public LocalDate getCreatedAt() { return createdAt; }
}