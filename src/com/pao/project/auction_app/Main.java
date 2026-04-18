package com.pao.project.auction_app;

import java.util.Scanner;

import com.pao.project.auction_app.models.users.User;
import com.pao.project.auction_app.services.UserService;
import com.pao.project.auction_app.utils.DataSeeder;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserService userService = UserService.getInstance();
    private static User currentUser = null;

    public static void main(String[] args) {
        System.out.println("\n-- Hai noroc! Spor la licitatii! --");

        while (true) {
            if (currentUser == null) {
                showGuestMenu();
            } else {
                showUserMenu();
            }
        }
    }

    private static void showGuestMenu() {
        System.out.println("1. Sign up");
        System.out.println("2. Login");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();

        clearConsole();

        switch (choice) {
            case "1" -> handleSignUp();
            case "2" -> handleLogin();
            case "0" -> {
                System.out.println("Goodbye!");
                System.exit(0);
            }
            case "s", "S" -> {
                System.out.println("\n[SISTEM] Seeding initial data...");
                DataSeeder.seedUsers();
            }
            default -> System.out.println("Invalid option. Please try again.");
        }
    }

    private static void handleSignUp() {
        System.out.println("Choose account type (Seller/Bidder): ");
        String type = scanner.nextLine();
        System.out.println("Enter username: ");
        String userName = scanner.nextLine();
        System.out.println("Enter email: ");
        String email = scanner.nextLine();
        System.out.println("Enter password: ");
        String password = scanner.nextLine();

        try {
            userService.signUp(type, userName, email, password);
            
            clearConsole();
            System.out.println("Sign up successful! You can now log in.");
        } catch (Exception e) {
            clearConsole();
            System.out.println("Error during sign up: " + e.getMessage());
        }
    }

    private static void handleLogin() {
        System.out.println("Enter email: ");
        String email = scanner.nextLine();
        System.out.println("Enter password: ");
        String password = scanner.nextLine();

        try {
            currentUser = userService.login(email, password);

            clearConsole();
            System.out.println("Login successful!");
        } catch (Exception e) {
            System.out.println("Error during login: " + e.getMessage());
        }
    }

    private static void showUserMenu() {
        System.out.println("\n--- MENU " + currentUser.getAccountType() + " ---");

        String type = currentUser.getAccountType();
        if (type.equalsIgnoreCase("Seller")) {
            System.out.println("1. Add Vehicle for Sale");
            System.out.println("2. Launch Auction");
            System.out.println("3. View My Vehicles");
        } else {
            System.out.println("1. View Active Auctions");
            System.out.println("2. Place a Bid");
            System.out.println("3. Check Balance");
        }

        System.out.println("0. Logout");
        System.out.print("Choose an option: ");

        String choice = scanner.nextLine();
        if (choice.equals("0")) {
            currentUser = null;

            clearConsole();
            System.out.println("Logged out successfully.");
        } else {
            clearConsole();
            System.out.println("Option not implemented yet. Please try again.");
        }
    }

    private static void clearConsole() {
        System.out.println("\033[H\033[2J\033[3J");
        System.out.flush();
    }
}
