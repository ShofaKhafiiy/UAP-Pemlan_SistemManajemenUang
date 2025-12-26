package com.ShofaKhafiy.MoneyManager.model;

/**
 * Abstract Category
 * Strategy Pattern
 */
public abstract class Category {

    protected String name;
    protected TransactionType type;

    protected Category(String name, TransactionType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public TransactionType getType() {
        return type;
    }

    /**
     * Polymorphic behavior
     */
    public abstract String getDisplayLabel();
}
