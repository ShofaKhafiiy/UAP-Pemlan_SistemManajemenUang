package com.ShofaKhafiy;

import com.ShofaKhafiy.MoneyManager.dao.ExcelFinancialRecordDAO;
import com.ShofaKhafiy.MoneyManager.dao.CsvUserDAO;
import com.ShofaKhafiy.MoneyManager.service.FinancialRecordService;
import com.ShofaKhafiy.MoneyManager.service.UserService;
import com.ShofaKhafiy.MoneyManager.util.ConsoleView;


public class Main {

    public static void main(String[] args) {
        // Inisialisasi DAO
        ExcelFinancialRecordDAO financialRecordDAO = new ExcelFinancialRecordDAO();
        CsvUserDAO userDAO = new CsvUserDAO("users.csv");

        // Inisialisasi Service
        FinancialRecordService financialRecordService = new FinancialRecordService(financialRecordDAO);
        UserService userService = new UserService(userDAO);

        // Inisialisasi View
        ConsoleView view = new ConsoleView(financialRecordService, userService);

        // Menampilkan menu utama
        view.displayMenu();  // Start the console view and menu
    }
}
