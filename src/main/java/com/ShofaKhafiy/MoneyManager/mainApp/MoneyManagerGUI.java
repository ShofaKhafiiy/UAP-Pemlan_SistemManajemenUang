package com.ShofaKhafiy.MoneyManager.view;

import com.ShofaKhafiy.MoneyManager.controller.TransactionController;
import com.ShofaKhafiy.MoneyManager.model.*;
import com.ShofaKhafiy.MoneyManager.service.MoneyManagerService;
import com.ShofaKhafiy.MoneyManager.util.FadeAnimation;
import com.ShofaKhafiy.MoneyManager.util.FormatUtil;
import com.ShofaKhafiy.MoneyManager.util.SimpleDocumentListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;

public class MoneyManagerGUI extends JFrame {

    private final MoneyManagerService service;
    private final TransactionController controller;
    private final FadePanel transactionPage = new FadePanel();

    private CardLayout cardLayout;
    private JPanel cardPanel;

    private JLabel balanceLabel;
    private JLabel userLabel; // Menampilkan siapa yang login
    private DefaultTableModel tableModel;

    private JComboBox<String> typeFilter;
    private JTextField keywordField;

    public MoneyManagerGUI() {
        service = MoneyManagerService.getInstance();
        controller = new TransactionController(this);

        initUI();
        refresh();
    }

    private void initUI() {
        setTitle("Money Manager - Dashboard");
        setSize(950, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // ================= MENU PAGE =================
        MenuPanel menuPanel = new MenuPanel(choice -> {
            switch (choice) {
                case "INCOME" -> controller.showIncomeDialog(this::refresh);
                case "EXPENSE" -> controller.showExpenseDialog(this::refresh);
                case "VIEW" -> {
                    cardLayout.show(cardPanel, "TRANSACTIONS");
                    FadeAnimation.fadeIn(transactionPage);
                }
                case "EXIT" -> handleLogout();
            }
        });

        // ================= TRANSACTION PAGE =================
        transactionPage.setLayout(new BorderLayout(10, 10));
        transactionPage.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        transactionPage.setBackground(Color.WHITE);

        // ===== HEADER (SALDO + FILTER) =====
        JPanel headerPanel = new JPanel(new BorderLayout(5, 5));
        headerPanel.setOpaque(false);

        // Baris Atas Header: Info User & Saldo
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setOpaque(false);

        userLabel = new JLabel("Login sebagai: " + service.getCurrentUser());
        userLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));

        balanceLabel = new JLabel("Total Saldo: Rp 0");
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        balanceLabel.setForeground(new Color(44, 62, 80));

        infoPanel.add(userLabel);
        infoPanel.add(balanceLabel);
        headerPanel.add(infoPanel, BorderLayout.NORTH);

        // Baris Bawah Header: Filter
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setOpaque(false);

        typeFilter = new JComboBox<>(new String[]{"SEMUA", "PEMASUKAN", "PENGELUARAN"});
        keywordField = new JTextField(15);

        filterPanel.add(new JLabel("Filter Tipe:"));
        filterPanel.add(typeFilter);
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(new JLabel("Cari Deskripsi:"));
        filterPanel.add(keywordField);

        headerPanel.add(filterPanel, BorderLayout.SOUTH);
        transactionPage.add(headerPanel, BorderLayout.NORTH);

        // ===== TABLE =====
        tableModel = new DefaultTableModel(
                new String[]{"Tanggal", "Tipe", "Kategori", "Deskripsi", "Jumlah", "Saldo Akhir"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(35);
        table.getTableHeader().setReorderingAllowed(false);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(52, 152, 219, 100));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        transactionPage.add(scrollPane, BorderLayout.CENTER);

        // ===== BOTTOM PANEL (BUTTONS) =====
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottom.setOpaque(false);

        StyledButton backBtn = new StyledButton("⬅ Kembali", new Color(149, 165, 166));
        StyledButton editBtn = new StyledButton("✏ Edit", new Color(241, 196, 15));
        StyledButton deleteBtn = new StyledButton("🗑 Hapus", new Color(192, 57, 43));
        StyledButton downloadBtn = new StyledButton("📥 Download Excel", new Color(46, 204, 113));

        backBtn.addActionListener(e -> cardLayout.show(cardPanel, "MENU"));
        editBtn.addActionListener(e -> updateSelected(table));
        deleteBtn.addActionListener(e -> deleteSelected(table));
        downloadBtn.addActionListener(e -> handleDownload());

        bottom.add(backBtn);
        bottom.add(editBtn);
        bottom.add(deleteBtn);
        bottom.add(downloadBtn);

        transactionPage.add(bottom, BorderLayout.SOUTH);

        // ================= MAIN ASSEMBLY =================
        cardPanel.add(menuPanel, "MENU");
        cardPanel.add(transactionPage, "TRANSACTIONS");

        add(cardPanel);
        cardLayout.show(cardPanel, "MENU");

        // ===== FILTER EVENTS =====
        typeFilter.addActionListener(e -> refresh());
        keywordField.getDocument().addDocumentListener(new SimpleDocumentListener(this::refresh));
    }

    private void refresh() {
        String keyword = keywordField.getText().toLowerCase();
        String type = (String) typeFilter.getSelectedItem();

        balanceLabel.setText("Total Saldo: " + FormatUtil.formatCurrency(service.getBalance()));
        tableModel.setRowCount(0);

        for (Transaction t : service.getAllTransactions()) {
            if (t.isDeleted()) continue;

            if (!"SEMUA".equals(type)) {
                if (type.equals("PEMASUKAN") && t.getType() != TransactionType.INCOME) continue;
                if (type.equals("PENGELUARAN") && t.getType() != TransactionType.EXPENSE) continue;
            }

            if (!t.getDescription().toLowerCase().contains(keyword)) continue;

            tableModel.addRow(new Object[]{
                    FormatUtil.formatDate(t.getCreatedAt()),
                    t.getType().getLabel(),
                    t.getCategory().getName(),
                    t.getDescription(),
                    FormatUtil.formatCurrency(t.getAmount()),
                    FormatUtil.formatCurrency(t.getBalanceAfter())
            });
        }
    }

    private void updateSelected(JTable table) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin diedit!");
            return;
        }

        Transaction oldTrx = service.getAllTransactions().get(row);
        TransactionEditDialog dialog = new TransactionEditDialog(this, oldTrx, updated -> {
            // Updated mengandung Nilai, Kategori, dan Deskripsi baru dari dialog
            service.updateTransaction(
                    oldTrx,
                    updated.getAmount(),
                    updated.getCategory(),
                    updated.getDescription()
            );
            refresh();
        });
        dialog.setVisible(true);
    }

    private void deleteSelected(JTable table) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin dihapus!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Hapus riwayat transaksi ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Transaction trx = service.getAllTransactions().get(row);
            service.deleteTransactionHistory(trx);
            refresh();
        }
    }

    private void handleDownload() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan Laporan Transaksi");
        fileChooser.setSelectedFile(new File("Laporan_Keuangan_" + service.getCurrentUser() + ".xlsx"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (service.exportHistory(fileToSave)) {
                JOptionPane.showMessageDialog(this, "Berhasil mengunduh laporan ke:\n" + fileToSave.getAbsolutePath());
            } else {
                JOptionPane.showMessageDialog(this, "Gagal mengunduh laporan. Guest Mode tidak mendukung export.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda ingin keluar?", "Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            service.logout();
            dispose();
            // Panggil kembali AppLauncher untuk menampilkan login (jika diperlukan)
            // AppLauncher.showLogin();
        }
    }
}