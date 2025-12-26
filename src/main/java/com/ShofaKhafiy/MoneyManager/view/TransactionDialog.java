package com.ShofaKhafiy.MoneyManager.view;

import com.ShofaKhafiy.MoneyManager.model.*;

import com.ShofaKhafiy.MoneyManager.service.CategoryFactory;
import com.ShofaKhafiy.MoneyManager.service.MoneyManagerService;
import com.ShofaKhafiy.MoneyManager.util.FormatUtil;

import javax.swing.*;
import java.awt.*;

public class TransactionDialog extends JDialog {

    private final MoneyManagerService service;
    private final TransactionType type;
    private final Runnable onSuccess;

    public TransactionDialog(JFrame parent,
                             MoneyManagerService service,
                             double currentBalance,
                             TransactionType type,
                             Runnable onSuccess) {

        super(parent, true);
        this.service = service;
        this.type = type;
        this.onSuccess = onSuccess;

        setTitle(type == TransactionType.INCOME
                ? "Tambah Pemasukan"
                : "Tambah Pengeluaran");

        setSize(420, 330);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // ================= SALDO =================
        JLabel saldoLabel = new JLabel(
                "Saldo Saat Ini: " + FormatUtil.formatCurrency(currentBalance)
        );
        saldoLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saldoLabel.setBorder(BorderFactory.createEmptyBorder(10, 15, 0, 15));

        saldoLabel.setForeground(
                type == TransactionType.INCOME
                        ? new Color(39, 174, 96)
                        : new Color(192, 57, 43)
        );

        add(saldoLabel, BorderLayout.NORTH);

        // ================= FORM =================
        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        JTextField amountField = new JTextField();
        JTextField descField = new JTextField();

        CategoryFactory factory = CategoryFactory.getInstance();

        String[] categories = (type == TransactionType.INCOME)
                ? factory.getIncomeCategoryNames()
                : factory.getExpenseCategoryNames();

        JComboBox<String> categoryBox =
                new JComboBox<>(categories);

        form.add(new JLabel("Jumlah"));
        form.add(amountField);
        form.add(new JLabel("Kategori"));
        form.add(categoryBox);
        form.add(new JLabel("Deskripsi"));
        form.add(descField);

        add(form, BorderLayout.CENTER);

        // ================= BUTTON =================
        JButton saveBtn = new JButton("Simpan");
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));

        saveBtn.addActionListener(e -> {
            String input = amountField.getText().trim();

            // Validasi kosong
            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Jumlah belum diisi",
                        "Peringatan",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // Validasi hanya angka (boleh desimal)
            if (!input.matches("\\d+(\\.\\d+)?")) {
                JOptionPane.showMessageDialog(
                        this,
                        "Jumlah harus berupa angka!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            try {
                double amount = Double.parseDouble(input);

                String categoryName = (String) categoryBox.getSelectedItem();
                Category category = factory.getCategory(categoryName, type);
                String description = descField.getText();

                TransactionResult result =
                        (type == TransactionType.INCOME)
                                ? service.addIncome(amount, category, description)
                                : service.addExpense(amount, category, description);

                if (!result.isSuccess()) {
                    JOptionPane.showMessageDialog(
                            this,
                            result.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                JOptionPane.showMessageDialog(
                        this,
                        result.getMessage(),
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE
                );

                onSuccess.run();
                dispose();

            } catch (NumberFormatException ex) {
                // fallback jika parsing gagal (meski regex sudah filter)
                JOptionPane.showMessageDialog(
                        this,
                        "Jumlah harus berupa angka!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });


        JPanel bottom = new JPanel();
        bottom.add(saveBtn);

        add(bottom, BorderLayout.SOUTH);
    }
}
