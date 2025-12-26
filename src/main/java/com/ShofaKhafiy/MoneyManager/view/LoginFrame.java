package com.ShofaKhafiy.MoneyManager.view;

import com.ShofaKhafiy.MoneyManager.view.components.GradientPanel;
import com.ShofaKhafiy.MoneyManager.view.components.StyledButton;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.function.BiFunction;

public class LoginFrame extends JFrame {

    private JPanel mainContainer;
    private JPanel leftPanel;
    private GradientPanel rightPanel;
    private final int BREAKPOINT = 750;

    public LoginFrame(BiFunction<String, String, Boolean> onLogin,
                      Runnable onShowRegister,
                      Runnable onGuest) {

        setTitle("Money Manager - Welcome");
        setSize(950, 650);
        setMinimumSize(new Dimension(500, 600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainContainer = new JPanel(new GridLayout(1, 2));

        // ================= LEFT PANEL (FORM) =================
        leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(Color.WHITE);

        JPanel formWrapper = new JPanel();
        formWrapper.setLayout(new BoxLayout(formWrapper, BoxLayout.Y_AXIS));
        formWrapper.setBackground(Color.WHITE);
        formWrapper.setBorder(new EmptyBorder(20, 40, 20, 40));

        // --- Header ---
        JLabel welcomeTxt = new JLabel("Selamat Datang");
        welcomeTxt.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeTxt.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeTxt.setForeground(new Color(44, 62, 80));

        JLabel subTxt = new JLabel("Silakan masuk ke akun Anda");
        subTxt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subTxt.setForeground(Color.GRAY);
        subTxt.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Load Icons (Gunakan ClassLoader) ---
        Icon userIcon = loadSmallIcon("/email.png");
        Icon passIcon = loadSmallIcon("/key.png");

        // --- Username Section ---
        // --- Username Section ---
        JLabel userLabel = new JLabel(" Username", userIcon, JLabel.LEFT);
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Ubah ke CENTER agar konsisten dengan wrapper
        userLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20)); // Batasi tinggi label

        JTextField userField = createStyledTextField();

        userField.setAlignmentX(Component.CENTER_ALIGNMENT); // Pastikan Center


        // --- Password Section ---
        // --- Bagian Password ---
        JLabel passLabel = new JLabel(" Password", passIcon, JLabel.LEFT);
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        passLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
// Tetap LEFT agar teks "Password" ada di pojok kiri atas kotak


        JPasswordField passField = createStyledPasswordField();
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
// passField akan otomatis menggunakan applyStyle yang sudah diset CENTER_ALIGNMENT & MAX_WIDTH

        // --- Buttons ---
        StyledButton loginBtn = new StyledButton("MASUK", new Color(52, 152, 219));
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton regBtn = new JButton("Belum punya akun? Daftar gratis");
        styleLinkButton(regBtn);

        JButton guestBtn = new JButton("Mode Guest (Data Sementara)");
        styleLinkButton(guestBtn);

        // 1. Header (Tengah)
        formWrapper.add(welcomeTxt);
        formWrapper.add(Box.createVerticalStrut(5));
        formWrapper.add(subTxt);
        formWrapper.add(Box.createVerticalStrut(40));

// 2. Username Section (Kiri)
        formWrapper.add(userLabel);
        formWrapper.add(Box.createVerticalStrut(8));
        formWrapper.add(userField);

        formWrapper.add(Box.createVerticalStrut(20));

// 3. Password Section (Kiri)
        formWrapper.add(passLabel); // Label menempel di kiri atas
        formWrapper.add(Box.createVerticalStrut(8));
        formWrapper.add(passField); // Field sejajar di bawah label

        formWrapper.add(Box.createVerticalStrut(35));

// 4. Aksi (Tengah)
        formWrapper.add(loginBtn);
        formWrapper.add(Box.createVerticalStrut(20));
        formWrapper.add(regBtn);
        formWrapper.add(guestBtn);

        leftPanel.add(formWrapper);

        // ================= RIGHT PANEL (BRANDING) =================
        rightPanel = new GradientPanel();
        rightPanel.setLayout(new GridBagLayout());

        JPanel brandWrapper = new JPanel();
        brandWrapper.setLayout(new BoxLayout(brandWrapper, BoxLayout.Y_AXIS));
        brandWrapper.setOpaque(false);

        JLabel logoLabel = new JLabel();
        try {
            // Gunakan ClassLoader untuk logo besar
            java.net.URL imgUrl = getClass().getResource("/logo.png");
            if (imgUrl != null) {
                ImageIcon rawIcon = new ImageIcon(imgUrl);
                Image scaledImg = rawIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                logoLabel.setIcon(new ImageIcon(scaledImg));
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            logoLabel.setText("💰");
            logoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 100));
        }
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel appName = new JLabel("Money Manager");
        appName.setFont(new Font("Segoe UI", Font.BOLD, 32));
        appName.setForeground(Color.WHITE);
        appName.setAlignmentX(Component.CENTER_ALIGNMENT);

        brandWrapper.add(logoLabel);
        brandWrapper.add(Box.createVerticalStrut(20));
        brandWrapper.add(appName);
        rightPanel.add(brandWrapper);

        mainContainer.add(leftPanel);
        mainContainer.add(rightPanel);
        add(mainContainer);

        // RESPONSIVE LOGIC
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (getWidth() < BREAKPOINT) {
                    rightPanel.setVisible(false);
                    mainContainer.setLayout(new GridLayout(1, 1));
                } else {
                    rightPanel.setVisible(true);
                    mainContainer.setLayout(new GridLayout(1, 2));
                }
                mainContainer.revalidate();
            }
        });

        // EVENTS
        loginBtn.addActionListener(e -> {
            if (onLogin.apply(userField.getText(), new String(passField.getPassword()))) dispose();
        });
        regBtn.addActionListener(e -> {
            onShowRegister.run();
            dispose();
        });
        guestBtn.addActionListener(e -> {
            onGuest.run();
            dispose();
        });
    }

    // --- Helper UI: Border Radius & Optimalisasi ---
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
        // SAMA DENGAN TOMBOL: Biarkan lebar memanjang maksimal (Integer.MAX_VALUE)
        // dengan tinggi tetap 45px
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        field.setPreferredSize(new Dimension(350, 45));

        // KUNCI: Set ke CENTER agar BoxLayout menariknya memenuhi lebar yang sama dengan tombol
        field.setAlignmentX(Component.CENTER_ALIGNMENT);

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
        } catch (Exception e) {
            System.err.println("Gagal memuat ikon: " + path);
        }
        return null;
    }

    // --- Inner Class untuk Border Radius ---
    private static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color color;

        RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

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