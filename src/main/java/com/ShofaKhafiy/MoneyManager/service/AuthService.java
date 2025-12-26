package com.ShofaKhafiy.MoneyManager.service;

import com.ShofaKhafiy.MoneyManager.model.User;
import com.ShofaKhafiy.MoneyManager.model.UserRepository;
import com.ShofaKhafiy.MoneyManager.repository.ExcelUserRepository;
import com.ShofaKhafiy.MoneyManager.util.ExcelUtil;

public class AuthService {

    private static AuthService instance;
    private final UserRepository userRepository;

    private AuthService() {
        // Inisialisasi direktori dan file akun pusat saat service pertama kali dibuat
        ExcelUtil.initDirectories();
        ExcelUtil.initAuthDatabase();
        this.userRepository = new ExcelUserRepository();
    }

    public static synchronized AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    // Metode Tambahan: Digunakan untuk memberi peringatan jika akun belum terdaftar
    public boolean userExists(String username) {
        return userRepository.exists(username);
    }

    public String register(String username, String password, String confirmPassword) {
        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            return "Username dan Password tidak boleh kosong!";
        }
        if (!password.equals(confirmPassword)) {
            return "Konfirmasi password tidak cocok!";
        }
        if (userRepository.exists(username)) {
            return "Username sudah terdaftar!";
        }

        userRepository.save(new User(username, password));
        return "SUCCESS";
    }

    public boolean login(String username, String password) {
        User user = userRepository.findByUsername(username);
        return user != null && user.getPassword().equals(password);
    }
}