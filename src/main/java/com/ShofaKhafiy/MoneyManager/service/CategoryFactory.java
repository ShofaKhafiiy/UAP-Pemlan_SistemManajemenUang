package com.ShofaKhafiy.MoneyManager.service;

import com.ShofaKhafiy.MoneyManager.model.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class CategoryFactory {

    private static CategoryFactory instance;

    private final Map<String, Category> incomeCategories = new LinkedHashMap<>();
    private final Map<String, Category> expenseCategories = new LinkedHashMap<>();

    private CategoryFactory() {
        // income
        addIncome("Gaji");
        addIncome("Bonus");
        addIncome("Investasi");
        addIncome("Lainnya");

        // expense
        addExpense("Makanan");
        addExpense("Transportasi");
        addExpense("Hiburan");
        addExpense("Tagihan");
        addExpense("Lainnya");
    }

    public static CategoryFactory getInstance() {
        if (instance == null) {
            instance = new CategoryFactory();
        }
        return instance;
    }

    private void addIncome(String name) {
        incomeCategories.put(name, new IncomeCategory(name));
    }

    private void addExpense(String name) {
        expenseCategories.put(name, new ExpenseCategory(name));
    }

    public String[] getIncomeCategoryNames() {
        return incomeCategories.keySet().toArray(new String[0]);
    }

    public String[] getExpenseCategoryNames() {
        return expenseCategories.keySet().toArray(new String[0]);
    }

    public Category getCategory(String name, TransactionType type) {
        return type == TransactionType.INCOME
                ? incomeCategories.get(name)
                : expenseCategories.get(name);
    }

    public String[] getCategoryNames(TransactionType type) {
        return type == TransactionType.INCOME ? getIncomeCategoryNames() : getExpenseCategoryNames();
    }
}
