package com.ShofaKhafiy.MoneyManager.view.components;

import com.ShofaKhafiy.MoneyManager.service.AuthService;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;

public class RegisterFrame extends JFrame {

    private JPanel mainContainer;
    private GradientPanel leftPanel; // Sekarang Branding di Kiri
    private JPanel rightPanel;       // Sekarang Form di Kanan
    private final int BREAKPOINT = 750;

    public RegisterFrame(Runnable onBackToLogin) {
        setTitle("Money Manager - Registrasi");
        setSize(950, 650);
        setMinimumSize(new Dimension(500, 600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainContainer = new JPanel(new GridLayout(1, 2));

        // ================= LEFT PANEL (BRANDING) =================
        leftPanel = new GradientPanel();
        leftPanel.setLayout(new GridBagLayout());

        JPanel brandWrapper = new JPanel();
        brandWrapper.setLayout(new BoxLayout(brandWrapper, BoxLayout.Y_AXIS));
        brandWrapper.setOpaque(false);

        JLabel logoLabel = new JLabel();
        try {
            java.net.URL imgUrl = getClass().getResource("/logo.png");
            if (imgUrl != null) {
                ImageIcon rawIcon = new ImageIcon(imgUrl);
                Image scaledImg = rawIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                logoLabel.setIcon(new ImageIcon(scaledImg));
            } else { throw new Exception(); }
        } catch (Exception e) {
            logoLabel.setText("💰");
            logoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 100));
            logoLabel.setForeground(Color.WHITE);
        }
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel appName = new JLabel("Money Manager");
        appName.setFont(new Font("Segoe UI", Font.BOLD, 32));
        appName.setForeground(Color.WHITE);
        appName.setAlignmentX(Component.CENTER_ALIGNMENT);

        brandWrapper.add(logoLabel);
        brandWrapper.add(Box.createVerticalStrut(20));
        brandWrapper.add(appName);
        leftPanel.add(brandWrapper);

        // ================= RIGHT PANEL (FORM) =================
        rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);

        JPanel formWrapper = new JPanel();
        formWrapper.setLayout(new BoxLayout(formWrapper, BoxLayout.Y_AXIS));
        formWrapper.setBackground(Color.WHITE);
        formWrapper.setPreferredSize(new Dimension(400, 580));

// --- Header ---
        JLabel titleTxt = new JLabel("Buat Akun");
        titleTxt.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleTxt.setForeground(new Color(44, 62, 80));
        titleTxt.setAlignmentX(Component.CENTER_ALIGNMENT); // Kunci simetris 1

        JLabel subTxt = new JLabel("Mulai kelola keuanganmu sekarang");
        subTxt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subTxt.setForeground(Color.GRAY);
        subTxt.setAlignmentX(Component.CENTER_ALIGNMENT); // Kunci simetris 2

// --- Input Fields ---
// Kita buat lebar standar 320 agar konsisten di tengah
        Dimension fieldDim = new Dimension(320, 45);
        Dimension labelDim = new Dimension(320, 25);

        Icon userIcon = loadSmallIcon("/email.png");
        Icon passIcon = loadSmallIcon("/key.png");

// Username
        JLabel userLabel = createFieldLabel(" Username", userIcon);
        userLabel.setMaximumSize(labelDim);
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Kunci simetris 3

        JTextField userField = createStyledTextField();
        userField.setMaximumSize(fieldDim);
        userField.setAlignmentX(Component.CENTER_ALIGNMENT);

// Password
        JLabel passLabel = createFieldLabel(" Password", passIcon);
        passLabel.setMaximumSize(labelDim);
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPasswordField passField = createStyledPasswordField();
        passField.setMaximumSize(fieldDim);
        passField.setAlignmentX(Component.CENTER_ALIGNMENT);

// Confirm Password
        JLabel confirmLabel = createFieldLabel(" Konfirmasi Password", passIcon);
        confirmLabel.setMaximumSize(labelDim);
        confirmLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPasswordField confirmField = createStyledPasswordField();
        confirmField.setMaximumSize(fieldDim);
        confirmField.setAlignmentX(Component.CENTER_ALIGNMENT);

// --- Buttons ---
        StyledButton regBtn = new StyledButton("DAFTAR SEKARANG", new Color(46, 204, 113));
        regBtn.setMaximumSize(new Dimension(320, 50));
        regBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton backBtn = new JButton("Sudah punya akun? Masuk di sini");
        styleLinkButton(backBtn);
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

// --- Susun ke formWrapper ---
        formWrapper.add(Box.createVerticalGlue()); // Fleksibel space atas
        formWrapper.add(titleTxt);
        formWrapper.add(Box.createVerticalStrut(5));
        formWrapper.add(subTxt);
        formWrapper.add(Box.createVerticalStrut(40));

        formWrapper.add(userLabel);
        formWrapper.add(Box.createVerticalStrut(5));
        formWrapper.add(userField);
        formWrapper.add(Box.createVerticalStrut(15));

        formWrapper.add(passLabel);
        formWrapper.add(Box.createVerticalStrut(5));
        formWrapper.add(passField);
        formWrapper.add(Box.createVerticalStrut(15));

        formWrapper.add(confirmLabel);
        formWrapper.add(Box.createVerticalStrut(5));
        formWrapper.add(confirmField);
        formWrapper.add(Box.createVerticalStrut(35));

        formWrapper.add(regBtn);
        formWrapper.add(Box.createVerticalStrut(20));
        formWrapper.add(backBtn);
        formWrapper.add(Box.createVerticalGlue()); // Fleksibel space bawah

// Tambahkan ke rightPanel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(formWrapper, gbc);
        // Masukkan ke mainContainer (Banding di kiri, Form di kanan)
        mainContainer.add(leftPanel);
        mainContainer.add(rightPanel);
        add(mainContainer);

        // RESPONSIVE LOGIC
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (getWidth() < BREAKPOINT) {
                    leftPanel.setVisible(false);
                    mainContainer.setLayout(new GridLayout(1, 1));
                } else {
                    leftPanel.setVisible(true);
                    mainContainer.setLayout(new GridLayout(1, 2));
                }
                mainContainer.revalidate();
            }
        });

        // EVENTS
        regBtn.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());
            String confirm = new String(confirmField.getPassword());

            String result = AuthService.getInstance().register(user, pass, confirm);
            if (result.equals("SUCCESS")) {
                JOptionPane.showMessageDialog(this, "Registrasi Berhasil! Silakan Login.");
                onBackToLogin.run();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backBtn.addActionListener(e -> { onBackToLogin.run(); dispose(); });
    }

    // --- Helper Methods ---
    private JLabel createFieldLabel(String text, Icon icon) {
        JLabel label = new JLabel(text, icon, JLabel.LEFT);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        applyStyle(field);
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        applyStyle(field);
        return field;
    }

    private void applyStyle(JTextField field) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        field.setPreferredSize(new Dimension(350, 45));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(15, new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
    }

    private void styleLinkButton(JButton btn) {
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(new Color(52, 152, 219));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private Icon loadSmallIcon(String path) {
        try {
            java.net.URL imgUrl = getClass().getResource(path);
            if (imgUrl != null) {
                ImageIcon icon = new ImageIcon(imgUrl);
                Image img = icon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
        } catch (Exception e) {}
        return null;
    }

    private static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color color;
        RoundedBorder(int radius, Color color) { this.radius = radius; this.color = color; }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(color);
            g2d.draw(new RoundRectangle2D.Double(x, y, width - 1, height - 1, radius, radius));
            g2d.dispose();
        }
    }
}