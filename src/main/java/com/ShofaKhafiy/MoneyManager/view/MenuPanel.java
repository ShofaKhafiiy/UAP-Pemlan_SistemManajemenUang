package com.ShofaKhafiy.MoneyManager.view;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/**
 * Menu utama RESPONSIVE
 */
public class MenuPanel extends JPanel {

    private RoundedPanel card;

    public MenuPanel(Consumer<String> onMenuSelected) {

        setLayout(new GridBagLayout());
        setBackground(new Color(245, 246, 250));

        card = new RoundedPanel(30);
        card.setBackground(Color.WHITE);
        card.setLayout(new GridLayout(6, 1, 18, 18));

        // ===== TITLE =====
        JLabel title = new JLabel("Money Manager", SwingConstants.CENTER);
        JLabel subtitle = new JLabel(
                "Kelola pemasukan & pengeluaran dengan mudah",
                SwingConstants.CENTER
        );

        JButton incomeBtn =
                createMenuButton("➕ Tambah Pemasukan", new Color(46, 204, 113));
        JButton expenseBtn =
                createMenuButton("➖ Tambah Pengeluaran", new Color(231, 76, 60));
        JButton viewBtn =
                createMenuButton("📄 Tampilkan Transaksi", new Color(52, 152, 219));
        JButton exitBtn =
                createMenuButton("🚪 Keluar", new Color(149, 165, 166));

        incomeBtn.addActionListener(e -> onMenuSelected.accept("INCOME"));
        expenseBtn.addActionListener(e -> onMenuSelected.accept("EXPENSE"));
        viewBtn.addActionListener(e -> onMenuSelected.accept("VIEW"));
        exitBtn.addActionListener(e -> onMenuSelected.accept("EXIT"));

        card.add(title);
        card.add(subtitle);
        card.add(incomeBtn);
        card.add(expenseBtn);
        card.add(viewBtn);
        card.add(exitBtn);

        add(card);

        // ===== RESPONSIVE HANDLER =====
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                resizeUI(title, subtitle);
            }
        });
    }

    // =====================================================
    // RESPONSIVE RESIZE
    // =====================================================
    private void resizeUI(JLabel title, JLabel subtitle) {

        int w = getWidth();
        int h = getHeight();

        // Card = 55% width, 70% height
        int cardW = (int) (w * 0.55);
        int cardH = (int) (h * 0.70);

        card.setPreferredSize(new Dimension(cardW, cardH));
        card.setBorder(
                BorderFactory.createEmptyBorder(
                        cardH / 12,
                        cardW / 10,
                        cardH / 12,
                        cardW / 10
                )
        );

        // Font scaling
        int titleSize = Math.max(22, w / 35);
        int subtitleSize = Math.max(13, w / 75);

        title.setFont(new Font("Segoe UI", Font.BOLD, titleSize));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, subtitleSize));

        revalidate();
        repaint();
    }

    // =====================================================
    // BUTTON FACTORY
    // =====================================================
    private JButton createMenuButton(String text, Color color) {

        JButton btn = new JButton(text);
        btn.setUI(new RoundedButtonUI(color));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(200, 48));

        return btn;
    }
}
