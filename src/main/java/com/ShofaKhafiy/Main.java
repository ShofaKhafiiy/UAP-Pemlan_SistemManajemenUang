package com.ShofaKhafiy;  // Pastikan ini sesuai dengan struktur direktori

import com.ShofaKhafiy.MoneyManager.dao.*;
import com.ShofaKhafiy.MoneyManager.model.*;
import com.ShofaKhafiy.MoneyManager.service.*;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize DAO and Services
        UserDAO userDAO = new CsvUserDAO("users.csv");
        TransactionDAO transactionDAO = new CsvTransactionDAO("transactions.csv");

        UserService userService = new UserService(userDAO);
        TransactionService transactionService = new TransactionService(transactionDAO);

        boolean running = true;

        while (running) {
            System.out.println("Welcome to Daily Management System");
            System.out.println("1. Register an account");
            System.out.println("2. Login");
            System.out.println("3. Continue as Guest");
            System.out.print("Please choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            User user = null;

            switch (choice) {
                case 1:
                    // Register a new user
                    System.out.print("Enter your username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter your password: ");
                    String password = scanner.nextLine();
                    user = userService.register(username, password);
                    System.out.println("Registration successful! Welcome " + user.getUsername());
                    break;
                case 2:
                    // Login
                    System.out.print("Enter your username: ");
                    username = scanner.nextLine();
                    System.out.print("Enter your password: ");
                    password = scanner.nextLine();
                    user = userService.login(username, password);

                    if (user != null) {
                        System.out.println("Login successful! Welcome back " + user.getUsername());
                    } else {
                        System.out.println("Invalid credentials. Please try again.");
                    }
                    break;
                case 3:
                    // Guest mode (no user data saved)
                    System.out.println("You are now using the system as a guest. No data will be saved.");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    continue; // Skip to next iteration if invalid choice
            }

            // Now, the user can interact with the application after login or guest access
            if (user != null || choice == 3) {
                boolean userRunning = true;
                while (userRunning) {
                    System.out.println("\nChoose an option:");
                    System.out.println("1. Deposit Money");
                    System.out.println("2. Withdraw Money");
                    System.out.println("3. View Transaction History");
                    System.out.println("4. Exit");
                    System.out.print("Enter your choice: ");
                    int action = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    switch (action) {
                        case 1:
                            // Deposit money
                            System.out.print("Enter amount to deposit: ");
                            double deposit = scanner.nextDouble();
                            scanner.nextLine(); // Consume newline
                            transactionService.addIncome(deposit, "Deposit");
                            break;
                        case 2:
                            // Withdraw money
                            System.out.print("Enter amount to withdraw: ");
                            double withdrawal = scanner.nextDouble();
                            scanner.nextLine(); // Consume newline
                            try {
                                transactionService.addExpense(withdrawal, "Withdrawal");
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getMessage());  // Display error if insufficient balance
                            }
                            break;
                        case 3:
                            // View transaction history
                            System.out.println("\nTransaction History:");
                            transactionService.getAllTransactions().forEach(t -> {
                                System.out.println("Transaction ID: " + t.getId());
                                System.out.println("Type: " + t.getType());
                                System.out.println("Amount: " + t.getAmount());
                                System.out.println("Balance After: " + t.getBalanceAfter());
                                System.out.println("Description: " + t.getDescription());
                                System.out.println("Date: " + t.getDate());
                                System.out.println("---------------------------");
                            });
                            break;
                        case 4:
                            // Exit
                            userRunning = false;
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                }
            }

            // Exit the main program if user decides to exit
            System.out.println("Thank you for using the Daily Management System!");
            running = false;
        }

        scanner.close();
    }
}
