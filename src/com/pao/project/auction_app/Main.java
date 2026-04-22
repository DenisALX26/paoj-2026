package com.pao.project.auction_app;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import com.pao.project.auction_app.models.engines.ThermalEngine;
import com.pao.project.auction_app.models.engines.enums.FuelType;
import com.pao.project.auction_app.models.users.User;
import com.pao.project.auction_app.models.vehicles.Vehicle;
import com.pao.project.auction_app.models.vehicles.cars.ThermalCar;
import com.pao.project.auction_app.models.vehicles.cars.enums.BodyType;
import com.pao.project.auction_app.models.vehicles.cars.enums.DriveType;
import com.pao.project.auction_app.services.EngineService;
import com.pao.project.auction_app.services.UserService;
import com.pao.project.auction_app.services.VehicleService;
import com.pao.project.auction_app.utils.DataSeeder;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserService userService = UserService.getInstance();
    private static final VehicleService vehicleService = VehicleService.getInstance();
    private static final EngineService engineService = EngineService.getInstance();
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
                DataSeeder.resetData();
                DataSeeder.seedAll();
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

        String userType = currentUser.getAccountType();
        if (userType.equalsIgnoreCase("Seller")) {
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
        switch (choice) {
            case "1" -> {
                if (userType.equalsIgnoreCase("Seller")) {
                    addVehicleForSale();
                } else {
                    System.out.println("View Active Auctions - Feature coming soon!");
                }
            }
            case "2" -> {
                if (userType.equalsIgnoreCase("Seller")) {
                    launchAuction();
                } else {
                    System.out.println("Place a Bid - Feature coming soon!");
                }
            }
            case "3" -> {
                if (userType.equalsIgnoreCase("Seller")) {
                    viewSellerVehicles(currentUser.getId(), false);
                } else {
                    System.out.println("Check Balance - Feature coming soon!");
                }
            }
            case "0" -> {
                currentUser = null;
                clearConsole();
                System.out.println("Logged out successfully.");
            }
            default -> System.out.println("Invalid option. Please try again.");

        }
    }

    private static void clearConsole() {
        System.out.println("\033[H\033[2J\033[3J");
        System.out.flush();
    }

    private static void showVehicleDetails(Vehicle vehicle, String choice) {
        clearConsole();
        System.out.println("\n--- Vehicle Details ---");
        System.out.println("Manufacturer: " + vehicle.getManufacturer());
        System.out.println("Model: " + vehicle.getModel());
        System.out.println("Year: " + vehicle.getYear());
        System.out.println("Mileage: " + vehicle.getMileage() + " km");
        System.out.println("Price: " + vehicle.getPrice() + " EUR");
        System.out.println("Engine Details: " + vehicle.getEngine().toString());
        System.out.println("\nPress 1 to edit price\nPress 2 to edit mileage\nPress Enter to go back...");
        choice = scanner.nextLine();
        switch (choice) {
            case "1" -> {
                System.out.println("Enter new price (EUR): ");
                int newPrice = Integer.parseInt(scanner.nextLine());
                vehicle.setPrice(newPrice);
                System.out.println("Price updated successfully!");
            }
            case "2" -> {
                System.out.println("Enter new mileage (km): ");
                int newMileage = Integer.parseInt(scanner.nextLine());
                vehicle.setMileage(newMileage);
                System.out.println("Mileage updated successfully!");
            }
            default -> {
                clearConsole();
                return;
            }
        }
        clearConsole();
    }

    private static void viewSellerVehicles(UUID sellerId, boolean filterActive) {
        clearConsole();
        System.out.println("\n--- My Vehicles for Sale ---");
        List<Vehicle> vehicles = filterActive
                ? vehicleService.getVehiclesByOwnerId(sellerId).stream().filter(Vehicle::isSellable).toList()
                : vehicleService.getVehiclesByOwnerId(sellerId);

        System.out.println(vehicles.size() + " vehicle(s) found.");

        boolean hasVehicles = !vehicles.isEmpty();

        while (true) {
            if (vehicles.isEmpty()) {
                System.out.println("You have no vehicles listed for sale.");
                hasVehicles = false;
            } else {
                vehicles.forEach(
                        vehicle -> System.out.printf("%d. %s\n", vehicles.indexOf(vehicle) + 1, vehicle.toString()));
            }

            if (!hasVehicles) {
                System.out.println("\n0. Back to Menu");
            } else {
                System.out.println("\n0. Back to Menu | Enter vehicle number to view details");
            }

            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();
            if (choice.equals("0")) {
                clearConsole();
                break;
            }
            showVehicleDetails(vehicles.get(Integer.parseInt(choice) - 1), choice);
        }

    }

    private static void addVehicleForSale() {
        clearConsole();
        while (true) {
            System.out.println(
                    "What type of vehicle do you want to sell?:\n1. Thermal Car\n2. Electric Car\n3. Hybrid Car\n4. Naked Motorcycle\n5. Sport Motorcycle\n\n0. Back to Menu | Enter option number");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> {
                    addThermalCar();
                    break;
                }
                case "2" -> {
                    System.out.println("Adding Electric Car - Feature coming soon!");
                    break;
                }
                case "3" -> {
                    System.out.println("Adding Hybrid Car - Feature coming soon!");
                    break;
                }
                case "4" -> {
                    System.out.println("Adding Naked Motorcycle - Feature coming soon!");
                    break;
                }
                case "5" -> {
                    System.out.println("Adding Sport Motorcycle - Feature coming soon!");
                    break;
                }
                case "0" -> {
                    clearConsole();
                    break;
                }
                default -> {
                    clearConsole();
                    System.out.println("Invalid option. Please try again.");
                }
            }
            return;
        }
    }

    private static void addThermalCar() {
        clearConsole();
        System.out.println("Enter Car Details:\nManufacturer: ");
        String manufacturer = scanner.nextLine();
        System.out.println("Model: ");
        String model = scanner.nextLine();
        System.out.println("Year: ");
        int year = Integer.parseInt(scanner.nextLine());
        System.out.println("Mileage (km): ");
        int mileage = Integer.parseInt(scanner.nextLine());
        System.out.println("Price (EUR): ");
        int price = Integer.parseInt(scanner.nextLine());
        System.out.println("Engine Power (HP): ");
        int power = Integer.parseInt(scanner.nextLine());
        System.out.println("Engine Torque (Nm): ");
        int torque = Integer.parseInt(scanner.nextLine());
        System.out.println("Fuel Type (PETROL/DIESEL): ");
        FuelType fuelType = FuelType.valueOf(scanner.nextLine().toUpperCase());
        System.out.println("Fuel Consumption (L/100km): ");
        double fuelConsumption = Double.parseDouble(scanner.nextLine());
        System.out.println("Number of Doors: ");
        int doors = Integer.parseInt(scanner.nextLine());
        System.out.println("Body Type (SEDAN/HATCHBACK/SUV): ");
        BodyType bodyType = BodyType.valueOf(scanner.nextLine().toUpperCase());
        System.out.println("Drive Type (FWD/RWD/AWD): ");
        DriveType driveType = DriveType.valueOf(scanner.nextLine().toUpperCase());
        System.out.println("Color: ");
        String color = scanner.nextLine();

        ThermalEngine engine = new ThermalEngine(power, torque, fuelType, fuelConsumption);
        ThermalCar newCar = new ThermalCar(manufacturer, model, year, price, mileage, engine, doors, bodyType,
                driveType, color);
        try {
            engineService.addEngine(engine);
            vehicleService.addVehicle(newCar, currentUser.getId());
            clearConsole();
            System.out.println("Thermal Car added successfully!");
        } catch (Exception e) {
            clearConsole();
            System.out.println("Error adding vehicle: " + e.getMessage());
        }
    }

    
}
