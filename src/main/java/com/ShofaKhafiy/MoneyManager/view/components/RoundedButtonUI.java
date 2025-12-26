package com.ShofaKhafiy.MoneyManager.view.components;

import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

public class RoundedButtonUI extends BasicButtonUI {

    private final Color base;
    private boolean hover;
    private boolean pressed;

    public RoundedButtonUI(Color base) {
        this.base = base;
    }

    public void setHover(boolean hover) {
        this.hover = hover;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    @Override
    public void paint(Graphics g, javax.swing.JComponent c) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        int arc = 18;

        Color bg = base;
        if (pressed) bg = bg.darker();
        else if (hover) bg = bg.brighter();

        g2.setColor(bg);
        g2.fillRoundRect(
                0, 0,
                c.getWidth(), c.getHeight(),
                arc, arc
        );

        super.paint(g, c);
    }
}
