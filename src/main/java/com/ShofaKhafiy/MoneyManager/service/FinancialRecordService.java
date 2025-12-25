package com.ShofaKhafiy.MoneyManager.service;

import com.ShofaKhafiy.MoneyManager.dao.ExcelFinancialRecordDAO;
import com.ShofaKhafiy.MoneyManager.model.FinancialRecord;
import com.ShofaKhafiy.MoneyManager.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class FinancialRecordService {

    private final ExcelFinancialRecordDAO financialRecordDAO;

    public FinancialRecordService(ExcelFinancialRecordDAO financialRecordDAO) {
        this.financialRecordDAO = financialRecordDAO;
    }

    public FinancialRecord addIncome(double amount, String description, User user) {
        String id = UUID.randomUUID().toString();
        // Periksa apakah user null (misalnya mode tamu)
        if (user == null) {
            // Jika user null, berarti ini adalah tamu, simpan transaksi sementara
            System.out.println("Income added for guest: " + description + " - " + amount);
            return new FinancialRecord(id, "income", amount, description, LocalDate.now());
        } else {
            // Jika user terdaftar, simpan transaksi ke file/Excel
            FinancialRecord record = new FinancialRecord(id, "income", amount, description, LocalDate.now());
            financialRecordDAO.save(record, user.getId());
            return record;
        }
    }

    public FinancialRecord addExpense(double amount, String description, User user) {
        String id = UUID.randomUUID().toString();
        // Periksa apakah user null (misalnya mode tamu)
        if (user == null) {
            // Jika user null, berarti ini adalah tamu, simpan transaksi sementara
            System.out.println("Expense added for guest: " + description + " - " + amount);
            return new FinancialRecord(id, "expense", amount, description, LocalDate.now());
        } else {
            // Jika user terdaftar, simpan transaksi ke file/Excel
            FinancialRecord record = new FinancialRecord(id, "expense", amount, description, LocalDate.now());
            financialRecordDAO.save(record, user.getId());
            return record;
        }
    }

    public void viewTransactionHistory(User user) {
        if (user != null) {
            List<FinancialRecord> records = financialRecordDAO.findAll(user.getId());
            records.forEach(record -> System.out.println(record.getType() + " | " + record.getAmount() + " | " + record.getDescription()));
        } else {
            System.out.println("You are using the system as a guest, no transaction history will be saved.");
        }
    }
}
