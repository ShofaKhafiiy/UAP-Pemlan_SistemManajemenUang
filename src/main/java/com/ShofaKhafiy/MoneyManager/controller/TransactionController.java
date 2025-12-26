package com.ShofaKhafiy.MoneyManager.controller;

import com.ShofaKhafiy.MoneyManager.model.*;
import com.ShofaKhafiy.MoneyManager.service.MoneyManagerService;
import com.ShofaKhafiy.MoneyManager.view.ModernInputDialog;
import javax.swing.*;

public class TransactionController {
    private final MoneyManagerService service;
    private final JFrame parent; // Kita gunakan variabel 'parent'

    public TransactionController(JFrame parent) {
        this.parent = parent;
        this.service = MoneyManagerService.getInstance();
    }

    public void showIncomeDialog(Runnable onSuccess) {
        String[] categories = {"Gaji", "Bonus", "Investasi", "Hadiah", "Lainnya"};
        ModernInputDialog dialog = new ModernInputDialog(parent, "Tambah Pemasukan", TransactionType.INCOME, categories);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Category category = new IncomeCategory(dialog.getSelectedCategoryName());
            TransactionResult result = service.addIncome(dialog.getAmount(), category, dialog.getDescription());
            handleResult(result, onSuccess);
        }
    }

    public void showExpenseDialog(Runnable onSuccess) {
        String[] categories = {"Makan", "Transportasi", "Belanja", "Tagihan", "Hiburan", "Lainnya"};
        ModernInputDialog dialog = new ModernInputDialog(parent, "Catat Pengeluaran", TransactionType.EXPENSE, categories);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Category category = new ExpenseCategory(dialog.getSelectedCategoryName());
            TransactionResult result = service.addExpense(dialog.getAmount(), category, dialog.getDescription());
            handleResult(result, onSuccess);
        }
    }

    public void handleEdit(Transaction t, Runnable callback) {
        String[] categories = (t.getType() == TransactionType.INCOME)
                ? new String[]{"Gaji", "Bonus", "Investasi", "Hadiah", "Lainnya"}
                : new String[]{"Makan", "Transportasi", "Belanja", "Tagihan", "Hiburan", "Lainnya"};

        ModernInputDialog dialog = new ModernInputDialog(parent, "Edit Transaksi", t.getType(), categories);

        // SINKRONISASI: Gunakan setInitialValues (bukan prefillData)
        String catName = (t.getCategory() != null) ? t.getCategory().getName() : "Lainnya";
        dialog.setInitialValues(t.getAmount(), catName, t.getDescription());

        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            try {
                t.setAmount(dialog.getAmount());
                t.setDescription(dialog.getDescription());

                // Set kategori baru berdasarkan tipe
                if (t.getType() == TransactionType.INCOME) {
                    t.setCategory(new IncomeCategory(dialog.getSelectedCategoryName()));
                } else {
                    t.setCategory(new ExpenseCategory(dialog.getSelectedCategoryName()));
                }

                service.updateTransaction(t);
                if (callback != null) callback.run();
                JOptionPane.showMessageDialog(parent, "Transaksi berhasil diperbarui!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, "Gagal: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void handleDelete(Transaction target, Runnable onSuccess) {
        int confirm = JOptionPane.showConfirmDialog(parent,
                "Hapus catatan '" + target.getDescription() + "'?", "Konfirmasi",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            service.deleteTransactionHistory(target);
            if (onSuccess != null) onSuccess.run();
        }
    }

    private void handleResult(TransactionResult result, Runnable onSuccess) {
        if (result.isSuccess()) {
            if (onSuccess != null) onSuccess.run();
        } else {
            JOptionPane.showMessageDialog(parent, result.getMessage(), "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }
}