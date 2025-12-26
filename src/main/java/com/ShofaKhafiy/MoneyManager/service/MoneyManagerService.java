package com.ShofaKhafiy.MoneyManager.service;

import com.ShofaKhafiy.MoneyManager.model.*;
import com.ShofaKhafiy.MoneyManager.repository.*;
import com.ShofaKhafiy.MoneyManager.util.ExcelUtil;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MoneyManagerService {
    private static MoneyManagerService instance;
    private TransactionRepository repository;
    private String currentUser = "GUEST";
    private boolean isGuestSession = true;
    private double balance = 0;

    private MoneyManagerService() {
        this.repository = new InMemoryTransactionRepo();
    }

    public static synchronized MoneyManagerService getInstance() {
        if (instance == null) instance = new MoneyManagerService();
        return instance;
    }

    public void setSession(String username, boolean isGuest) {
        this.currentUser = username;
        this.isGuestSession = isGuest;
        if (isGuest) {
            this.repository = new InMemoryTransactionRepo();
        } else {
            this.repository = new ExcelTransactionRepository(username);
        }
        recalculateBalance();
    }

    public TransactionResult addIncome(double amount, Category category, String desc) {
        if (amount <= 0) return TransactionResult.failure("Jumlah tidak valid");
        balance += amount;
        Transaction trx = new Transaction.Builder(UUID.randomUUID().toString(), TransactionType.INCOME)
                .owner(currentUser).amount(amount).category(category).description(desc)
                .balanceAfter(balance).build();
        repository.save(trx);
        return TransactionResult.success("Pemasukan berhasil", trx);
    }

    public TransactionResult addExpense(double amount, Category category, String desc) {
        if (amount <= 0) return TransactionResult.failure("Jumlah tidak valid");
        if (amount > balance) return TransactionResult.failure("Saldo tidak cukup!");
        balance -= amount;
        Transaction trx = new Transaction.Builder(UUID.randomUUID().toString(), TransactionType.EXPENSE)
                .owner(currentUser).amount(amount).category(category).description(desc)
                .balanceAfter(balance).build();
        repository.save(trx);
        return TransactionResult.success("Pengeluaran berhasil", trx);
    }

    public void deleteTransactionHistory(Transaction target) {
        List<Transaction> all = repository.findAll();
        repository.clear();
        for (Transaction t : all) {
            if (t.getId().equals(target.getId())) {
                repository.save(new Transaction.Builder(t.getId(), t.getType())
                        .owner(t.getOwner()).amount(t.getAmount()).category(t.getCategory())
                        .description(t.getDescription()).balanceAfter(t.getBalanceAfter())
                        .setCreatedAt(t.getCreatedAt()).deleted(true).build());
            } else {
                repository.save(t);
            }
        }
        recalculateBalance();
    }

    public void recalculateBalance() {
        balance = 0;
        List<Transaction> all = repository.findAll();
        repository.clear();
        for (Transaction t : all) {
            double newBalanceAfter = t.getBalanceAfter();
            if (t.getOwner().equals(currentUser)) {
                // Transaksi yang dihapus (soft delete) tetap dihitung saldonya agar riwayat saldo konsisten
                if (t.getType() == TransactionType.INCOME) balance += t.getAmount();
                else balance -= t.getAmount();
                newBalanceAfter = balance;
            }
            repository.save(new Transaction.Builder(t.getId(), t.getType())
                    .owner(t.getOwner()).amount(t.getAmount()).category(t.getCategory())
                    .description(t.getDescription()).balanceAfter(newBalanceAfter)
                    .deleted(t.isDeleted()).setCreatedAt(t.getCreatedAt()).build());
        }
    }

    /**
     * PERBAIKAN LOGIKA EDIT:
     * Menangani perubahan jumlah pemasukan maupun pengeluaran dengan validasi saldo.
     */
    public TransactionResult updateTransaction(Transaction oldTrx, double newAmount, Category newCategory, String newDesc) {
        if (newAmount <= 0) return TransactionResult.failure("Jumlah baru tidak valid");

        // 1. Hitung dampak perubahan terhadap saldo saat ini
        double diff;
        if (oldTrx.getType() == TransactionType.INCOME) {
            // Jika pemasukan berubah dari 100 ke 120, diff = +20 (saldo bertambah)
            diff = newAmount - oldTrx.getAmount();
        } else {
            // Jika pengeluaran berubah dari 50 ke 80, diff = -30 (saldo berkurang)
            diff = oldTrx.getAmount() - newAmount;
        }

        // 2. Validasi: Apakah perubahan ini membuat saldo akhir jadi negatif?
        if (balance + diff < 0) {
            return TransactionResult.failure("Gagal Edit: Saldo tidak mencukupi untuk perubahan pengeluaran ini!");
        }

        // 3. Proses Update di Repository
        List<Transaction> all = repository.findAll();
        repository.clear();
        for (Transaction t : all) {
            if (t.getId().equals(oldTrx.getId())) {
                Transaction updated = new Transaction.Builder(t.getId(), t.getType())
                        .owner(t.getOwner())
                        .amount(newAmount)
                        .category(newCategory)
                        .description(newDesc)
                        .deleted(t.isDeleted())
                        .setCreatedAt(t.getCreatedAt())
                        .build();
                repository.save(updated);
            } else {
                repository.save(t);
            }
        }

        recalculateBalance(); // Update saldo internal 'balance' dan simpan ke Excel
        return TransactionResult.success("Update berhasil", null);
    }

    public List<Transaction> getAllTransactions() {
        return repository.findAll().stream()
                .filter(t -> t.getOwner().equals(currentUser) && !t.isDeleted())
                .collect(Collectors.toList());
    }

    public double getBalance() { return balance; }
    public String getCurrentUser() { return currentUser; }

    public void logout() {
        currentUser = "GUEST";
        isGuestSession = true;
        this.repository = new InMemoryTransactionRepo();
        balance = 0;
    }

    public void reset() {
        this.balance = 0;
        if (isGuestSession) repository.clear();
    }

    public boolean exportHistory(File destinationPath) {
        if (isGuestSession) return false;
        try {
            File source = new File(ExcelUtil.getUserFilePath(currentUser));
            Files.copy(source.toPath(), destinationPath.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}