package com.ShofaKhafiy.MoneyManager.view.components;

import com.ShofaKhafiy.MoneyManager.model.*;
import com.ShofaKhafiy.MoneyManager.service.CategoryFactory;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class TransactionEditDialog extends JDialog {

    public TransactionEditDialog(JFrame parent,
                                 Transaction trx,
                                 Consumer<Transaction> onSave) {

        super(parent, "Edit Transaksi", true);
        setSize(400, 350); // Ukuran sedikit diperbesar untuk menampung field kategori
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Form Fields
        JTextField amountField = new JTextField(String.valueOf(trx.getAmount()));
        JTextField descField = new JTextField(trx.getDescription());

        // Setup Category ComboBox
        CategoryFactory factory = CategoryFactory.getInstance();
        String[] categories = factory.getCategoryNames(trx.getType());
        JComboBox<String> categoryBox = new JComboBox<>(categories);

        // Set kategori yang sedang terpilih saat ini
        if (trx.getCategory() != null) {
            categoryBox.setSelectedItem(trx.getCategory().getName());
        }

        // Layout Form (Baris ditambah menjadi 3)
        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        form.add(new JLabel("Jumlah"));
        form.add(amountField);

        form.add(new JLabel("Kategori"));
        form.add(categoryBox);

        form.add(new JLabel("Deskripsi"));
        form.add(descField);

        // Button Simpan
        JButton save = new JButton("Simpan Perubahan");
        save.setFont(new Font("Segoe UI", Font.BOLD, 13));

        save.addActionListener(e -> {
            try {
                String input = amountField.getText().trim();
                if (input.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Jumlah tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                double amount = Double.parseDouble(input);

                // Ambil objek kategori dari factory berdasarkan pilihan user
                String selectedCategoryName = (String) categoryBox.getSelectedItem();
                Category selectedCategory = factory.getCategory(selectedCategoryName, trx.getType());

                Transaction updated = new Transaction.Builder(
                        trx.getId(),
                        trx.getType()
                )
                        .amount(amount)
                        .category(selectedCategory) // Kategori sekarang dinamis
                        .description(descField.getText())
                        .balanceAfter(trx.getBalanceAfter())
                        .build();

                onSave.accept(updated);
                dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Jumlah tidak valid! Gunakan angka.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(form, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(save);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}