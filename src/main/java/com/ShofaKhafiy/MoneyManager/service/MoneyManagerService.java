package com.ShofaKhafiy.MoneyManager.service;

import com.ShofaKhafiy.MoneyManager.model.*;
import com.ShofaKhafiy.MoneyManager.repository.*;
import com.ShofaKhafiy.MoneyManager.util.ExcelUtil;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
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

        // Saldo sementara (akan diperbaiki total di recalculateBalance)
        balance += amount;

        Transaction trx = new Transaction.Builder(UUID.randomUUID().toString(), TransactionType.INCOME)
                .owner(currentUser).amount(amount).category(category).description(desc)
                .balanceAfter(balance).build();
        repository.save(trx);
        recalculateBalance(); // Sinkronisasi total
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
        recalculateBalance();
        return TransactionResult.success("Pengeluaran berhasil", trx);
    }

    public void deleteTransactionHistory(Transaction target) {
        List<Transaction> all = repository.findAll();
        repository.clear();
        for (Transaction t : all) {
            if (t.getId().equals(target.getId())) {
                // Buat objek baru dengan status deleted = true
                Transaction softDeleted = new Transaction.Builder(t.getId(), t.getType())
                        .owner(t.getOwner()).amount(t.getAmount()).category(t.getCategory())
                        .description(t.getDescription()).balanceAfter(t.getBalanceAfter())
                        .setCreatedAt(t.getCreatedAt())
                        .deleted(true) // Status terhapus dari riwayat
                        .build();
                repository.save(softDeleted);
            } else {
                repository.save(t);
            }
        }
        // JANGAN kurangi saldo di sini, biarkan recalculateBalance yang menghitung
        recalculateBalance();
    }

    /**
     * LOGIKA RECALCULATE:
     * Menghitung saldo dari awal (Income - Expense)
     * Termasuk transaksi yang di-soft-delete (isDeleted = true)
     */
    public void recalculateBalance() {
        balance = 0;
        List<Transaction> all = new ArrayList<>(repository.findAll());
        repository.clear();

        for (Transaction t : all) {
            if (t.getOwner().equals(currentUser)) {
                // 1. Hitung saldo
                if (t.getType() == TransactionType.INCOME) balance += t.getAmount();
                else balance -= t.getAmount();

                // 2. Proteksi Kategori Null agar tidak crash saat save
                Category safeCategory = t.getCategory();
                if (safeCategory == null) {
                    if (t.getType() == TransactionType.INCOME)
                        safeCategory = new IncomeCategory("Lainnya");
                    else
                        safeCategory = new ExpenseCategory("Lainnya");
                }

                Transaction updatedTrx = new Transaction.Builder(t.getId(), t.getType())
                        .owner(t.getOwner())
                        .amount(t.getAmount())
                        .category(safeCategory) // Gunakan kategori yang sudah diproteksi
                        .description(t.getDescription())
                        .balanceAfter(balance)
                        .deleted(t.isDeleted())
                        .setCreatedAt(t.getCreatedAt())
                        .build();
                repository.save(updatedTrx);
            } else {
                repository.save(t);
            }
        }
    }

    /**
     * PERBAIKAN: Menyesuaikan dengan call dari TransactionController (1 Parameter)
     */
    public TransactionResult updateTransaction(Transaction updatedTrx) {
        if (updatedTrx.getAmount() <= 0) return TransactionResult.failure("Jumlah tidak valid");

        List<Transaction> all = repository.findAll();
        repository.clear();

        for (Transaction t : all) {
            if (t.getId().equals(updatedTrx.getId())) {
                repository.save(updatedTrx);
            } else {
                repository.save(t);
            }
        }

        recalculateBalance(); // Saldo otomatis ter-update secara global

        // Cek jika setelah update saldo jadi negatif
        if (balance < 0) {
            // Rollback atau beri peringatan (opsional tergantung kebutuhan)
            return TransactionResult.failure("Peringatan: Saldo menjadi negatif setelah perubahan ini!");
        }

        return TransactionResult.success("Update berhasil", updatedTrx);
    }

    /**
     * Tampilan tabel hanya mengambil yang belum di-soft-delete
     */
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