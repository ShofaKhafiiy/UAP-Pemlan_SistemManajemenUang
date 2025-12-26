package com.ShofaKhafiy.MoneyManager.view;

import javax.swing.*;
import java.awt.*;

public class StyledButton extends JButton {

    private final Color bg;

    public StyledButton(String text, Color bg) {
        super(text);
        this.bg = bg;

        setFocusPainted(false);
        setContentAreaFilled(false);
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setColor(bg);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // 🔵 RADIUS

        super.paintComponent(g);
        g2.dispose();
    }
}
