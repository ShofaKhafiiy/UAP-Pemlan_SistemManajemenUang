package com.ShofaKhafiy.MoneyManager.view;

import javax.swing.*;
import java.awt.*;

public class FadePanel extends JPanel {

    private float alpha = 1f;

    public void setAlpha(float alpha) {
        this.alpha = alpha;
        repaint();
    }

    public float getAlpha() {
        return alpha;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(
                AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER, alpha
                )
        );
        super.paintComponent(g2);
        g2.dispose();
    }
}
