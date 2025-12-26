package com.ShofaKhafiy.MoneyManager.model;

public class IncomeCategory extends Category {

    public IncomeCategory(String name) {
        super(name, TransactionType.INCOME);
    }

    @Override
    public String getDisplayLabel() {
        return "Pemasukan - " + name;
    }
}
