package com.ShofaKhafiy.MoneyManager.mainApp;

import com.ShofaKhafiy.MoneyManager.service.*;
import com.ShofaKhafiy.MoneyManager.view.*;
import com.ShofaKhafiy.MoneyManager.util.ExcelUtil;
import com.ShofaKhafiy.MoneyManager.view.components.RegisterFrame;

import javax.swing.*;

public class AppLauncher {

    public static void main(String[] args) {
        // Inisialisasi folder dan database akun pusat
        ExcelUtil.initDirectories();
        ExcelUtil.initAuthDatabase();

        // Mengatur Look and Feel sistem (opsional, agar UI lebih native)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            showLogin();
        });
    }

    public static void showLogin() {
        LoginFrame loginFrame = new LoginFrame(
                // Parameter 1: Logika Login (onLogin)
                (user, pass) -> {
                    if (user.isEmpty() || pass.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Username dan Password tidak boleh kosong!");
                        return false;
                    }

                    AuthService auth = AuthService.getInstance();

                    // CEK LOGIK: Apakah akun ada?
                    if (!auth.userExists(user)) {
                        JOptionPane.showMessageDialog(null,
                                "Akun '" + user + "' belum terdaftar!\nSilakan klik 'Daftar gratis' terlebih dahulu.",
                                "Login Gagal", JOptionPane.WARNING_MESSAGE);
                        return false;
                    }

                    // CEK PASSWORD
                    if (auth.login(user, pass)) {
                        // 1. Set sesi user di service
                        MoneyManagerService.getInstance().setSession(user, false);

                        // 2. Tampilkan MAIN FRAME (Dashboard Baru dengan Sidebar)
                        // Menggantikan MoneyManagerGUI lama
                        new MainFrame().setVisible(true);

                        return true; // Menutup LoginFrame (sesuai logika di LoginFrame.java)
                    } else {
                        JOptionPane.showMessageDialog(null, "Password salah!", "Login Gagal", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                },
                // Parameter 2: Pindah ke Register
                () -> showRegister(),
                // Parameter 3: Guest Mode
                () -> {
                    MoneyManagerService.getInstance().setSession("GUEST", true);
                    MoneyManagerService.getInstance().reset();

                    // Tampilkan MAIN FRAME untuk Guest
                    new MainFrame().setVisible(true);
                }
        );
        loginFrame.setVisible(true);
    }

    public static void showRegister() {
        // Tetap menggunakan showLogin() sebagai callback setelah daftar sukses
        RegisterFrame regFrame = new RegisterFrame(() -> showLogin());
        regFrame.setVisible(true);
    }
}