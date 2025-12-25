package com.ShofaKhafiy.MoneyManager.service;

import com.ShofaKhafiy.MoneyManager.dao.TransactionDAO;
import com.ShofaKhafiy.MoneyManager.model.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class TransactionService {

    private final TransactionDAO transactionDAO;
    private double balance;

    // ================= CONSTRUCTOR =================

    public TransactionService(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
        recalculateBalance();  // Recalculate balance when service is created
    }

    // ================= CREATE =================

    public Transaction addIncome(double amount, String description) {
        validateAmount(amount);

        // Increase balance for income transaction
        balance += amount;

        // Create a new income transaction
        Transaction trx = new Transaction(
                UUID.randomUUID().toString(),
                "income",  // Transaction type is "income"
                amount,
                description,
                balance,
                LocalDate.now()  // Date is today
        );

        // Save the transaction
        transactionDAO.save(trx);
        return trx;
    }

    public Transaction addExpense(double amount, String description) {
        validateAmount(amount);

        // Ensure balance is sufficient before allowing expense
        if (amount > balance) {
            throw new IllegalArgumentException("Saldo tidak mencukupi");
        }

        // Decrease balance for expense transaction
        balance -= amount;

        // Create a new expense transaction
        Transaction trx = new Transaction(
                UUID.randomUUID().toString(),
                "expense",  // Transaction type is "expense"
                amount,
                description,
                balance,
                LocalDate.now()  // Date is today
        );

        // Save the transaction
        transactionDAO.save(trx);
        return trx;
    }

    // ================= READ =================

    public List<Transaction> getAllTransactions() {
        return transactionDAO.findAll();
    }

    public double getBalance() {
        return balance;
    }

    // ================= UPDATE =================

    public void updateTransaction(String id, double newAmount, String newDescription) {
        validateAmount(newAmount);

        List<Transaction> all = transactionDAO.findAll();
        transactionDAO.clear();  // Clear the current data and start fresh

        balance = 0;  // Reset the balance before recalculating

        for (Transaction t : all) {
            Transaction rebuilt = t;

            if (t.getId().equals(id)) {
                rebuilt = new Transaction(
                        t.getId(),
                        t.getType(),
                        newAmount,
                        newDescription,
                        0,  // Reset balance after transaction update
                        LocalDate.now()  // Date is today
                );
            }

            // Recalculate the balance after every transaction
            balance = rebuilt.getType().equals("income")
                    ? balance + rebuilt.getAmount()
                    : balance - rebuilt.getAmount();

            rebuilt = new Transaction(
                    rebuilt.getId(),
                    rebuilt.getType(),
                    rebuilt.getAmount(),
                    rebuilt.getDescription(),
                    balance,
                    rebuilt.getDate()
            );

            transactionDAO.save(rebuilt);  // Save the updated transaction
        }
    }

    // ================= DELETE =================

    public void deleteTransaction(String id) {
        List<Transaction> all = transactionDAO.findAll();
        transactionDAO.clear();  // Clear the current data

        balance = 0;  // Reset the balance before recalculating

        for (Transaction t : all) {
            if (t.getId().equals(id)) continue;  // Skip the transaction that is deleted

            // Recalculate the balance after each remaining transaction
            balance = t.getType().equals("income")
                    ? balance + t.getAmount()
                    : balance - t.getAmount();

            transactionDAO.save(t);  // Save the remaining transactions with updated balance
        }
    }

    // ================= INTERNAL =================

    private void validateAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Jumlah harus > 0");
        }
    }

    private void recalculateBalance() {
        balance = 0;  // Start with a balance of 0
        for (Transaction t : transactionDAO.findAll()) {
            balance = t.getType().equals("income")
                    ? balance + t.getAmount()
                    : balance - t.getAmount();
        }
    }
}
