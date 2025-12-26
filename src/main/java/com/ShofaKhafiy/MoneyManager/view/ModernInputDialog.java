package com.ShofaKhafiy.MoneyManager.view;

import com.ShofaKhafiy.MoneyManager.model.TransactionType;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class ModernInputDialog extends JDialog {
    private JTextField txtAmount;
    private JTextField txtDesc;
    private JComboBox<String> cbCategory;
    private boolean confirmed = false;

    // Warna yang nyaman di mata (Soft Palette)
    private final Color primaryColor;
    private final Color bgColor = new Color(252, 253, 255);
    private final Color textColor = new Color(44, 62, 80);
    private final Color secondaryText = new Color(149, 165, 166);


    public ModernInputDialog(Frame owner, String title, TransactionType type, String[] categories) {
        super(owner, title, true);

        // Hijau lembut untuk Income, Merah Karang untuk Expense
        this.primaryColor = (type == TransactionType.INCOME) ? new Color(39, 174, 96) : new Color(231, 76, 60);

        setSize(420, 520);
        setLocationRelativeTo(owner);
        setUndecorated(true);

        // Panel Utama dengan Border Lembut
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(bgColor);
        root.setBorder(BorderFactory.createLineBorder(new Color(230, 236, 240), 1));

        // --- HEADER ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(primaryColor);
        header.setPreferredSize(new Dimension(420, 80));

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        header.add(lblTitle, BorderLayout.CENTER);

        // --- BODY ---
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(35, 45, 20, 45));

        // Field Nominal
        addLabel(body, "NOMINAL (Rp)");
        txtAmount = createModernField("0");
        body.add(txtAmount);
        body.add(Box.createVerticalStrut(25));

        // Field Kategori
        addLabel(body, "KATEGORI");
        cbCategory = new JComboBox<>(categories);
        styleComboBox(cbCategory);
        body.add(cbCategory);
        body.add(Box.createVerticalStrut(25));

        // Field Catatan (Tidak Wajib)
        addLabel(body, "CATATAN (Opsional)");
        txtDesc = createModernField("Tambahkan keterangan di sini...");
        body.add(txtDesc);

        // --- FOOTER ---
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 25));
        footer.setOpaque(false);


        JButton btnCancel = createStyledButton("BATAL", new Color(189, 195, 199));
        btnCancel.addActionListener(e -> dispose());

        JButton btnSave = createStyledButton("SIMPAN", primaryColor);
        btnSave.addActionListener(e -> {
            if(validateInput()) {
                confirmed = true;
                dispose();
            }
        });

        footer.add(btnCancel);
        footer.add(btnSave);

        root.add(header, BorderLayout.NORTH);
        root.add(body, BorderLayout.CENTER);
        root.add(footer, BorderLayout.SOUTH);
        add(root);
    }

    private JTextField createModernField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setForeground(textColor);
        field.setCaretColor(primaryColor);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(220, 225, 230)),
                new EmptyBorder(8, 0, 8, 0)
        ));





        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 2, 0, primaryColor),
                        new EmptyBorder(8, 0, 8, 0)
                ));
            }
            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(220, 225, 230)),
                        new EmptyBorder(8, 0, 8, 0)
                ));
            }
        });
        return field;
    }

    public void setInitialValues(double amount, String category, String desc) {
        txtAmount.setText(String.valueOf((int)amount));
        cbCategory.setSelectedItem(category);
        txtDesc.setText(desc.equals("-") ? "" : desc);
    }



    private void styleComboBox(JComboBox<String> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(220, 225, 230)));
        ((JLabel)combo.getRenderer()).setHorizontalAlignment(SwingConstants.LEFT);
    }
    public String getSelectedCategoryName() {
        return (String) cbCategory.getSelectedItem();
    }


    private void addLabel(JPanel panel, String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(secondaryText);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(4));
    }

    private JButton createStyledButton(String text, Color baseColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(baseColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(130, 40));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(baseColor.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(baseColor); }
        });
        return btn;
    }

    private boolean validateInput() {
        try {
            double amount = Double.parseDouble(txtAmount.getText().replace(".", ""));
            if (amount <= 0) throw new Exception();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Masukkan nominal yang valid!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // --- GETTERS ---
    public boolean isConfirmed() { return confirmed; }
    public double getAmount() {
        try {
            return Double.parseDouble(txtAmount.getText().replace(".", ""));
        } catch (NumberFormatException e) { return 0; }
    }
    public String getDescription() {
        String desc = txtDesc.getText().trim();
        return (desc.isEmpty() || desc.equals("Tambahkan keterangan di sini...")) ? "-" : desc;
    }
    public String getCategory() { return (String) cbCategory.getSelectedItem(); }


}