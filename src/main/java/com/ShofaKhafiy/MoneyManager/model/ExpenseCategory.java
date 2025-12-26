package com.ShofaKhafiy.MoneyManager.model;

public class ExpenseCategory extends Category {

    public ExpenseCategory(String name) {
        super(name, TransactionType.EXPENSE);
    }

    @Override
    public String getDisplayLabel() {
        return "Pengeluaran - " + name;
    }
}
