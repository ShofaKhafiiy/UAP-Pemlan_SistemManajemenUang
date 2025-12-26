package com.ShofaKhafiy.MoneyManager.view;

import com.ShofaKhafiy.MoneyManager.controller.TransactionController;
import com.ShofaKhafiy.MoneyManager.model.Transaction;
import com.ShofaKhafiy.MoneyManager.model.TransactionType;
import com.ShofaKhafiy.MoneyManager.service.MoneyManagerService;
import com.ShofaKhafiy.MoneyManager.util.FormatUtil;
import com.ShofaKhafiy.MoneyManager.util.SimpleDocumentListener;
import com.ShofaKhafiy.MoneyManager.view.components.RoundedButtonUI;
import com.ShofaKhafiy.MoneyManager.view.components.RoundedPanel;
import com.ShofaKhafiy.MoneyManager.view.components.StyledButton;
import com.ShofaKhafiy.MoneyManager.view.components.SummaryCard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    private final MoneyManagerService service;
    private final TransactionController controller;

    private RoundedPanel alertPanel;
    private JTextPane txtTipContent;
    private JLabel lblTrxCount;
    private SimpleChartPanel chartPanel;
    private Timer tipTimer;
    private String[] quotes;
    private final double LOW_BALANCE_THRESHOLD = 100000.0;

    private JPanel sidebar;
    private JPanel contentArea;
    private CardLayout cardLayout;

    private SummaryCard cardSaldo, cardIncome, cardExpense;
    private JLabel lblWelcome;

    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> typeFilter;
    private JTextField keywordField;

    private List<Transaction> filteredTransactions = new ArrayList<>();

    public MainFrame() {
        this.service = MoneyManagerService.getInstance();
        this.controller = new TransactionController(this);

        setTitle("Money Manager Pro - Dashboard Interaktif");
        setSize(1200, 850);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(242, 244, 247));

        setLayout(new BorderLayout());

        initSidebar();

        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setOpaque(false);

        contentArea.add(createDashboardPanel(), "DASHBOARD");
        contentArea.add(createHistoryPanel(), "VIEW");

        add(sidebar, BorderLayout.WEST);
        add(contentArea, BorderLayout.CENTER);

        refreshData();
    }

    private void initSidebar() {
        sidebar = new JPanel(new GridBagLayout());
        sidebar.setBackground(new Color(23, 32, 42));
        sidebar.setPreferredSize(new Dimension(260, getHeight()));

        JPanel menuWrapper = new JPanel(new GridLayout(7, 1, 0, 10));
        menuWrapper.setOpaque(false);

        JLabel brand = new JLabel("MONEY MANAGER", SwingConstants.CENTER);
        brand.setFont(new Font("Segoe UI", Font.BOLD, 20));
        brand.setForeground(Color.WHITE);
        brand.setBorder(new EmptyBorder(0, 0, 30, 0));

        menuWrapper.add(brand);
        menuWrapper.add(createSidebarButton("📊 Dashboard", "DASHBOARD", new Color(52, 152, 219)));
        menuWrapper.add(new JSeparator());
        menuWrapper.add(createSidebarButton("➕ Pemasukan", "INC", new Color(46, 204, 113)));
        menuWrapper.add(createSidebarButton("➖ Pengeluaran", "EXP", new Color(231, 76, 60)));
        menuWrapper.add(createSidebarButton("📄 Riwayat", "VIEW", new Color(155, 89, 182)));
        menuWrapper.add(createSidebarButton("🚪 Keluar", "OUT", new Color(149, 165, 166)));

        sidebar.add(menuWrapper);
    }

    private JPanel createDashboardPanel() {
        JPanel dashboard = new JPanel(new BorderLayout(0, 30));
        dashboard.setOpaque(false);
        dashboard.setBorder(new EmptyBorder(40, 40, 40, 40));

        // --- HEADER ---
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        lblWelcome = new JLabel(getTimeGreeting() + ", " + service.getCurrentUser());
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 34));
        lblWelcome.setForeground(new Color(28, 40, 51));

        alertPanel = new RoundedPanel(15);
        alertPanel.setBackground(new Color(255, 230, 230));
        alertPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 12));
        alertPanel.setVisible(false);
        alertPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        alertPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { cardLayout.show(contentArea, "VIEW"); }
        });

        JLabel alertTxt = new JLabel("⚠️ SALDO RENDAH: Klik untuk cek detail transaksi.");
        alertTxt.setFont(new Font("Segoe UI", Font.BOLD, 13));
        alertTxt.setForeground(new Color(176, 58, 46));
        alertPanel.add(alertTxt);

        header.add(lblWelcome, BorderLayout.NORTH);
        header.add(alertPanel, BorderLayout.SOUTH);

        // --- BODY ---
        JPanel body = new JPanel(new BorderLayout(0, 30));
        body.setOpaque(false);

        // Summary Cards
        JPanel cards = new JPanel(new GridLayout(1, 3, 25, 0));
        cards.setOpaque(false);
        cardSaldo = new SummaryCard("Total Saldo", "Rp 0", new Color(41, 128, 185), "💰");
        cardIncome = new SummaryCard("Pemasukan", "Rp 0", new Color(39, 174, 96), "📈");
        cardExpense = new SummaryCard("Pengeluaran", "Rp 0", new Color(192, 57, 43), "📉");
        cards.add(cardSaldo); cards.add(cardIncome); cards.add(cardExpense);

        // Chart & Tips Row
        JPanel midRow = new JPanel(new GridLayout(1, 2, 25, 0));
        midRow.setOpaque(false);

        chartPanel = new SimpleChartPanel(); // Inisialisasi Chart
        midRow.add(chartPanel);
        midRow.add(createTipsPanel());

        body.add(cards, BorderLayout.NORTH);
        body.add(midRow, BorderLayout.CENTER);

        lblTrxCount = new JLabel("📊 Status: Memproses data...");
        lblTrxCount.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 13));
        lblTrxCount.setForeground(new Color(86, 101, 115));
        body.add(lblTrxCount, BorderLayout.SOUTH);

        dashboard.add(header, BorderLayout.NORTH);
        dashboard.add(body, BorderLayout.CENTER);

        return dashboard;
    }

    public void refreshData() {
        // 1. Ambil data (Saldo tetap diambil dari service yang mencatat total historis)
        double bal = service.getBalance();
        List<Transaction> allTransactions = service.getAllTransactions();

        // 2. Update Kartu Saldo Utama
        cardSaldo.setValue(FormatUtil.formatCurrency(bal));
        alertPanel.setVisible(bal < LOW_BALANCE_THRESHOLD && bal > 0);

        // 3. Hitung Total In/Out untuk Chart & Kartu
        // HAPUS pengecekan isDeleted() di sini agar angka tetap permanen
        double totalIn = 0;
        double totalOut = 0;
        for (Transaction t : allTransactions) {
            // Kita hitung SEMUA transaksi, termasuk yang sudah dihapus dari riwayat
            if (t.getType() == TransactionType.INCOME) {
                totalIn += t.getAmount();
            } else {
                totalOut += t.getAmount();
            }
        }

        cardIncome.setValue(FormatUtil.formatCurrency(totalIn));
        cardExpense.setValue(FormatUtil.formatCurrency(totalOut));
        if (chartPanel != null) chartPanel.updateData(totalIn, totalOut);

        // 4. Update Tabel (Hanya di sini kita cek isDeleted)
        if (tableModel != null) {
            tableModel.setRowCount(0);
            filteredTransactions.clear();

            String kw = (keywordField != null) ? keywordField.getText().toLowerCase() : "";
            String typeSelect = (typeFilter != null) ? (String)typeFilter.getSelectedItem() : "SEMUA";

            for (Transaction t : allTransactions) {
                // Transaksi yang dihapus HANYA hilang dari tabel
                if (t.isDeleted()) continue;

                String categoryName = (t.getCategory() != null) ? t.getCategory().getName() : "Lain-lain";

                boolean matchType = typeSelect.equals("SEMUA") ||
                        (typeSelect.equals("PEMASUKAN") && t.getType() == TransactionType.INCOME) ||
                        (typeSelect.equals("PENGELUARAN") && t.getType() == TransactionType.EXPENSE);

                boolean matchKeyword = t.getDescription().toLowerCase().contains(kw) ||
                        categoryName.toLowerCase().contains(kw);

                if (matchType && matchKeyword) {
                    filteredTransactions.add(t);
                    tableModel.addRow(new Object[]{
                            FormatUtil.formatDate(t.getCreatedAt()),
                            t.getType().getLabel(),
                            categoryName,
                            t.getDescription(),
                            FormatUtil.formatCurrency(t.getAmount()),
                            FormatUtil.formatCurrency(t.getBalanceAfter())
                    });
                }
            }
        }

        if (lblTrxCount != null) {
            lblTrxCount.setText("📊 Menampilkan " + filteredTransactions.size() + " catatan riwayat.");
        }

        revalidate();
        repaint();
    }

    private JPanel createHistoryPanel() {
        JPanel history = new JPanel(new BorderLayout(20, 20));
        history.setOpaque(false);
        history.setBorder(new EmptyBorder(30, 40, 30, 40));

        // FILTER
        JPanel filter = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filter.setOpaque(false);

        typeFilter = new JComboBox<>(new String[]{"SEMUA", "PEMASUKAN", "PENGELUARAN"});
        keywordField = new JTextField(15);
        keywordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));

        filter.add(new JLabel("Tipe:")); filter.add(typeFilter);
        filter.add(Box.createHorizontalStrut(20));
        filter.add(new JLabel("Cari:")); filter.add(keywordField);

        // TABLE
        tableModel = new DefaultTableModel(new String[]{"Tanggal", "Tipe", "Kategori", "Catatan", "Jumlah", "Saldo"}, 0);
        table = new JTable(tableModel);
        table.setRowHeight(45);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(new Color(52, 152, 219, 40));
        table.setSelectionForeground(Color.BLACK);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Popup Menu
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem editItem = new JMenuItem("📝 Edit Transaksi");
        JMenuItem deleteItem = new JMenuItem("🗑️ Hapus Transaksi");
        popupMenu.add(editItem);
        popupMenu.addSeparator();
        popupMenu.add(deleteItem);

        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { handlePopup(e); }
            public void mouseReleased(MouseEvent e) { handlePopup(e); }
            private void handlePopup(MouseEvent e) {
                if (e.isPopupTrigger() && table.getSelectedRow() != -1) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        editItem.addActionListener(e -> {
            Transaction t = filteredTransactions.get(table.getSelectedRow());
            controller.handleEdit(t, this::refreshData);
        });
        deleteItem.addActionListener(e -> {
            Transaction t = filteredTransactions.get(table.getSelectedRow());
            controller.handleDelete(t, this::refreshData);
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(Color.WHITE);

        // FOOTER
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);

        JPanel leftActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        leftActions.setOpaque(false);
        JButton btnEdit = new StyledButton("Edit", new Color(52, 152, 219));
        JButton btnHapus = new StyledButton("Hapus", new Color(231, 76, 60));
        leftActions.add(btnEdit); leftActions.add(btnHapus);

        JButton btnExcel = new StyledButton("📥 Simpan Excel", new Color(46, 204, 113));

        footer.add(leftActions, BorderLayout.WEST);
        footer.add(btnExcel, BorderLayout.EAST);

        // LISTENERS
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) controller.handleEdit(filteredTransactions.get(row), this::refreshData);
            else JOptionPane.showMessageDialog(this, "Pilih transaksi yang ingin diubah.");
        });

        btnHapus.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) controller.handleDelete(filteredTransactions.get(row), this::refreshData);
            else JOptionPane.showMessageDialog(this, "Pilih transaksi yang ingin dihapus.");
        });

        btnExcel.addActionListener(e -> handleDownload());
        keywordField.getDocument().addDocumentListener(new SimpleDocumentListener(this::refreshData));
        typeFilter.addActionListener(e -> refreshData());

        history.add(filter, BorderLayout.NORTH);
        history.add(sp, BorderLayout.CENTER);
        history.add(footer, BorderLayout.SOUTH);
        return history;
    }

    private JPanel createTipsPanel() {
        RoundedPanel panel = new RoundedPanel(25);
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout(20, 15));
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("💡 Tips Keuangan");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(52, 152, 219));

        txtTipContent = new JTextPane();
        txtTipContent.setEditable(false);
        txtTipContent.setOpaque(false);
        txtTipContent.setContentType("text/html");

        quotes = new String[]{
                "Gunakan formula <b>50/30/20</b>: 50% Kebutuhan, 30% Keinginan, 20% Tabungan.",
                "Catat setiap pengeluaran kecil untuk menghindari kebocoran dana.",
                "Simpan dana darurat setidaknya 3-6 bulan gaji Anda.",
                "Hindari berhutang untuk membeli barang yang nilainya cepat turun.",
                "Investasikan sisa uang Anda ke instrumen aman seperti Reksadana."
        };

        updateTipText(quotes[0]);
        panel.add(title, BorderLayout.NORTH);
        panel.add(txtTipContent, BorderLayout.CENTER);

        if (tipTimer != null) tipTimer.stop();
        tipTimer = new Timer(10000, e -> updateTipText(quotes[(int)(Math.random() * quotes.length)]));
        tipTimer.start();
        return panel;
    }

    private void updateTipText(String text) {
        txtTipContent.setText("<html><body style='font-family: Segoe UI; font-size: 13px; color: #2C3E50;'>" + text + "</body></html>");
    }

    private JButton createSidebarButton(String text, String cmd, Color c) {
        JButton btn = new JButton(text);
        btn.setUI(new RoundedButtonUI(c));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(new Color(200, 200, 200));
        btn.setPreferredSize(new Dimension(210, 48));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);

        btn.addActionListener(e -> {
            if(cmd.equals("DASHBOARD")) cardLayout.show(contentArea, "DASHBOARD");
            else if(cmd.equals("INC")) controller.showIncomeDialog(this::refreshData);
            else if(cmd.equals("EXP")) controller.showExpenseDialog(this::refreshData);
            else if(cmd.equals("VIEW")) cardLayout.show(contentArea, "VIEW");
            else if(cmd.equals("OUT")) handleLogout();
        });
        return btn;
    }

    private void handleDownload() {
        JFileChooser fc = new JFileChooser();
        if(fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            if(service.exportHistory(fc.getSelectedFile())) JOptionPane.showMessageDialog(this, "Berhasil disimpan!");
        }
    }

    private String getTimeGreeting() {
        int h = LocalTime.now().getHour();
        if(h < 12) return "Selamat Pagi";
        if(h < 17) return "Selamat Siang";
        return "Selamat Malam";
    }

    private void handleLogout() {
        if(JOptionPane.showConfirmDialog(this, "Keluar dari aplikasi?") == 0) {
            if(tipTimer != null) tipTimer.stop();
            service.logout();
            dispose();
        }
    }
}