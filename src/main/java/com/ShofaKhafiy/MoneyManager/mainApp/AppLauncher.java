package com.ShofaKhafiy.MoneyManager.mainApp;

import com.ShofaKhafiy.MoneyManager.service.*;
import com.ShofaKhafiy.MoneyManager.view.*;
import com.ShofaKhafiy.MoneyManager.util.ExcelUtil;
import com.ShofaKhafiy.MoneyManager.view.MoneyManagerGUI;

import javax.swing.*;

public class AppLauncher {

    public static void main(String[] args) {
        // Inisialisasi folder dan database akun pusat
        ExcelUtil.initDirectories();
        ExcelUtil.initAuthDatabase();

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
                        MoneyManagerService.getInstance().setSession(user, false);
                        new MoneyManagerGUI().setVisible(true);
                        return true;
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
                    new MoneyManagerGUI().setVisible(true);
                }
        );
        loginFrame.setVisible(true);
    }

    public static void showRegister() {
        RegisterFrame regFrame = new RegisterFrame(() -> showLogin());
        regFrame.setVisible(true);
    }
}