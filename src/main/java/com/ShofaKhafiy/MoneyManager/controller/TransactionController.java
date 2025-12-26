package com.ShofaKhafiy.MoneyManager.controller;

import com.ShofaKhafiy.MoneyManager.model.TransactionType;
import com.ShofaKhafiy.MoneyManager.service.MoneyManagerService;
import com.ShofaKhafiy.MoneyManager.view.TransactionDialog;

import javax.swing.*;

/**
 * Controller untuk menangani dialog transaksi
 * (Income & Expense)
 */
public class TransactionController {

    private final MoneyManagerService service;
    private final JFrame parent;

    public TransactionController(JFrame parent) {
        this.parent = parent;
        this.service = MoneyManagerService.getInstance();
    }


    // ================= INCOME =================
    public void showIncomeDialog(Runnable onSuccess) {

        TransactionDialog dialog =
                new TransactionDialog(
                        parent,
                        service,
                        service.getBalance(),        // ✅ saldo ditampilkan
                        TransactionType.INCOME,
                        onSuccess
                );

        dialog.setVisible(true);
    }

    // ================= EXPENSE =================
    public void showExpenseDialog(Runnable onSuccess) {

        TransactionDialog dialog =
                new TransactionDialog(
                        parent,
                        service,
                        service.getBalance(),        // ✅ saldo ditampilkan
                        TransactionType.EXPENSE,
                        onSuccess
                );

        dialog.setVisible(true);
    }
}
