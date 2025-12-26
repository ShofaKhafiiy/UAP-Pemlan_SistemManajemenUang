package com.ShofaKhafiy.MoneyManager.util;

import com.ShofaKhafiy.MoneyManager.view.components.FadePanel;

import javax.swing.*;

public class FadeAnimation {

    public static void fadeIn(FadePanel panel) {

        panel.setAlpha(0.3f);

        Timer timer = new Timer(15, null);

        timer.addActionListener(e -> {
            float alpha = panel.getAlpha() + 0.03f;
            if (alpha >= 1f) {
                panel.setAlpha(1f);
                timer.stop();
            } else {
                panel.setAlpha(alpha);
            }
        });

        timer.start();
    }
}
