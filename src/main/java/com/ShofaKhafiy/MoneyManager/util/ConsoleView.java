package com.ShofaKhafiy.MoneyManager.util;

import com.ShofaKhafiy.MoneyManager.model.User;
import com.ShofaKhafiy.MoneyManager.model.FinancialRecord;
import com.ShofaKhafiy.MoneyManager.service.FinancialRecordService;
import com.ShofaKhafiy.MoneyManager.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleView {

    private final FinancialRecordService financialRecordService;
    private final UserService userService;
    private final Scanner scanner = new Scanner(System.in);

    private List<FinancialRecord> guestTransactions = new ArrayList<>();  // Daftar transaksi sementara untuk tamu

    public ConsoleView(FinancialRecordService financialRecordService, UserService userService) {
        this.financialRecordService = financialRecordService;
        this.userService = userService;
    }

    public void displayMenu() {
        User currentUser = null;
        boolean running = true;

        while (running) {
            System.out.println("Welcome to Daily Management System");
            System.out.println("1. Login");
            System.out.println("2. Register an account");
            System.out.println("3. Continue as Guest");
            System.out.print("Please choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    // Login
                    System.out.print("Enter your username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter your password: ");
                    String password = scanner.nextLine();
                    currentUser = userService.login(username, password);

                    if (currentUser != null) {
                        System.out.println("Login successful! Welcome back " + currentUser.getUsername());
                        running = false;
                    } else {
                        System.out.println("Invalid credentials. Please try again.");
                    }
                    break;
                case 2:
                    // Register a new account
                    System.out.print("Enter a new username: ");
                    username = scanner.nextLine();
                    System.out.print("Enter a new password: ");
                    password = scanner.nextLine();
                    currentUser = userService.register(username, password);
                    System.out.println("Registration successful! Welcome " + currentUser.getUsername());
                    running = false;
                    break;
                case 3:
                    // Guest mode (no data saved)
                    System.out.println("You are now using the system as a guest. No data will be saved.");
                    currentUser = null;  // No user data saved for guest
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        // After login/registration/guest mode, show options for managing financial records
        if (currentUser != null) {
            showFinancialOptions(currentUser);
        } else {
            System.out.println("Exiting as guest. No data saved.");
            guestFinancialOptions();
        }
    }

    // Show guest options where no transactions are saved
    private void guestFinancialOptions() {
        boolean userRunning = true;
        while (userRunning) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. View Transaction History");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            int action = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (action) {
                case 1:
                    System.out.print("Enter income amount: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Enter description: ");
                    String description = scanner.nextLine();
                    // Tambahkan transaksi sementara untuk tamu
                    FinancialRecord income = new FinancialRecord("guest_income_" + amount, "income", amount, description, java.time.LocalDate.now());
                    guestTransactions.add(income);
                    System.out.println("Income added for guest: " + description + " - " + amount);
                    break;
                case 2:
                    System.out.print("Enter expense amount: ");
                    amount = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Enter description: ");
                    description = scanner.nextLine();
                    // Tambahkan transaksi sementara untuk tamu
                    FinancialRecord expense = new FinancialRecord("guest_expense_" + amount, "expense", amount, description, java.time.LocalDate.now());
                    guestTransactions.add(expense);
                    System.out.println("Expense added for guest: " + description + " - " + amount);
                    break;
                case 3:
                    // Menampilkan riwayat transaksi tamu
                    if (guestTransactions.isEmpty()) {
                        System.out.println("No transactions for guest.");
                    } else {
                        System.out.println("Guest Transactions:");
                        for (FinancialRecord record : guestTransactions) {
                            System.out.println(record.getType() + " | " + record.getAmount() + " | " + record.getDescription() + " | " + record.getDate());
                        }
                    }
                    break;
                case 4:
                    userRunning = false;
                    break;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }

    private void showFinancialOptions(User user) {
        boolean userRunning = true;
        while (userRunning) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. View Transaction History");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            int action = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (action) {
                case 1:
                    System.out.print("Enter income amount: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Enter description: ");
                    String description = scanner.nextLine();
                    financialRecordService.addIncome(amount, description, user);
                    break;
                case 2:
                    System.out.print("Enter expense amount: ");
                    amount = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Enter description: ");
                    description = scanner.nextLine();
                    financialRecordService.addExpense(amount, description, user);
                    break;
                case 3:
                    // View transaction history
                    financialRecordService.viewTransactionHistory(user);
                    break;
                case 4:
                    userRunning = false;
                    break;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }
}
