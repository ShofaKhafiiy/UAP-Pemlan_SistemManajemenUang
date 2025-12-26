package com.ShofaKhafiy.MoneyManager.view.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SummaryCard extends RoundedPanel { // MENGGUNAKAN ROUNDEDPANEL MILIK ANDA
    private JLabel lblValue;

    public SummaryCard(String title, String initialValue, Color accentColor, String icon) {
        super(25); // Set radius 25
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel textGroup = new JPanel();
        textGroup.setLayout(new BoxLayout(textGroup, BoxLayout.Y_AXIS));
        textGroup.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(new Color(149, 165, 166));

        lblValue = new JLabel(initialValue);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblValue.setForeground(accentColor);

        textGroup.add(lblTitle);
        textGroup.add(Box.createVerticalStrut(10));
        textGroup.add(lblValue);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 40));

        add(textGroup, BorderLayout.CENTER);
        add(iconLabel, BorderLayout.EAST);
    }

    public void setValue(String val) {
        lblValue.setText(val);
    }
}