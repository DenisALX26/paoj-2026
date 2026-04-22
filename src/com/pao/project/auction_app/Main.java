package com.pao.project.auction_app;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

import com.pao.project.auction_app.models.auctions.*;
import com.pao.project.auction_app.models.engines.*;
import com.pao.project.auction_app.models.engines.enums.FuelType;
import com.pao.project.auction_app.models.users.*;
import com.pao.project.auction_app.models.vehicles.Vehicle;
import com.pao.project.auction_app.models.vehicles.cars.*;
import com.pao.project.auction_app.models.vehicles.cars.enums.*;
import com.pao.project.auction_app.models.vehicles.motorcycles.*;
import com.pao.project.auction_app.models.vehicles.motorcycles.enums.*;
import com.pao.project.auction_app.services.*;
import com.pao.project.auction_app.utils.DataSeeder;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserService userService = UserService.getInstance();
    private static final VehicleService vehicleService = VehicleService.getInstance();
    private static final EngineService engineService = EngineService.getInstance();
    private static final AuctionService auctionService = AuctionService.getInstance();
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
            System.out.println("4. View My Cars");
            System.out.println("5. View My Motorcycles");
            System.out.println("6. View My Active Auctions");
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
                    printAllAuctions();
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
                    viewSellerVehicles(currentUser.getId(), false, Optional.empty());
                } else {
                    checkBalance();
                }
            }
            case "4" -> {
                if (userType.equalsIgnoreCase("Seller")) {
                    viewSellerVehicles(currentUser.getId(), false, Optional.of("car"));
                }
            }
            case "5" -> {
                if (userType.equalsIgnoreCase("Seller")) {
                    viewSellerVehicles(currentUser.getId(), false, Optional.of("motorcycle"));
                }
            }
            case "6" -> {
                if (userType.equalsIgnoreCase("Seller")) {
                    viewSellerAuctions(currentUser.getId());
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
        if (vehicle instanceof Motorcycle) {
            System.out.println("ABS: " + (((Motorcycle) vehicle).isHasABS() ? "Yes" : "No"));
            System.out.println("A2 Compatible: " + (((Motorcycle) vehicle).isA2Compatible() ? "Yes" : "No"));
            System.out.println("Number of Cylinders: " + ((Motorcycle) vehicle).getNumberOfCylinders());
            if (vehicle instanceof Naked) {
                System.out.println("Street Fighter: " + (((Naked) vehicle).isStreetFighter() ? "Yes" : "No"));
                System.out.println("Riding Modes: " + String.join(", ", ((Naked) vehicle).getRidingModes().toString()));
                System.out.println("Headlight Type: " + ((Naked) vehicle).getHeadlightType());
            } else if (vehicle instanceof Sport) {
                System.out.println("Cornering ABS: " + (((Sport) vehicle).hasCorneringABS() ? "Yes" : "No"));
                System.out.println("Quick Shifter: " + (((Sport) vehicle).hasQuickShifter() ? "Yes" : "No"));
            }
        } else if (vehicle instanceof Car) {
            System.out.println("Number of Doors: " + ((Car) vehicle).getNumberOfDoors());
            System.out.println("Body Type: " + ((Car) vehicle).getBodyType());
            System.out.println("Drive Type: " + ((Car) vehicle).getDriveType());
            System.out.println("Color: " + ((Car) vehicle).getColor());
        }
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

    private static void viewSellerVehicles(UUID sellerId, boolean sellableOnly, Optional<String> vehicleType) {
        clearConsole();
        System.out.println("\n--- My Vehicles for Sale ---");
        
        List<Vehicle> vehicles = vehicleService.getVehiclesByOwnerId(sellerId).stream()
                .filter(v -> !sellableOnly || v.isSellable())
                .filter(v -> vehicleType
                        .map(type -> {
                            if (type.equalsIgnoreCase("car"))
                                return v instanceof Car;
                            else if (type.equalsIgnoreCase("motorcycle"))
                                return v instanceof Motorcycle;
                            else
                                return true;
                        })
                        .orElse(true))
                .toList();

        boolean hasVehicles = !vehicles.isEmpty();

        while (true) {
            if (vehicles.isEmpty()) {
                System.out.println("You have no vehicles listed for sale.");
                hasVehicles = false;
            } else {
                for (int i = 0; i < vehicles.size(); i++) {
                    System.out.printf("%d. %s\n", i + 1, vehicles.get(i).toString());
                }
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
                    addElectricCar();
                    break;
                }
                case "3" -> {
                    addHybridCar();
                    break;
                }
                case "4" -> {
                    addNakedMotorcycle();
                    break;
                }
                case "5" -> {
                    addSportMotorcycle();
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

    private static void addSportMotorcycle() {
        clearConsole();
        System.out.println("Enter Motorcycle Details:\nManufacturer: ");
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
        FuelType fuelType = FuelType.PETROL;
        System.out.println("Fuel Consumption (L/100km): ");
        double fuelConsumption = Double.parseDouble(scanner.nextLine());
        System.out.println("Engine Capacity (cc): ");
        Double engineCapacity = Double.parseDouble(scanner.nextLine());
        System.out.println("Weight (kg): ");
        Double weight = Double.parseDouble(scanner.nextLine());
        System.out.println("Has ABS? (yes/no): ");
        boolean hasABS = scanner.nextLine().equalsIgnoreCase("yes");
        System.out.println("Is A2 Compatible? (yes/no): ");
        boolean isA2Compatible = scanner.nextLine().equalsIgnoreCase("yes");
        System.out.println("Number of Cylinders: ");
        int numberOfCylinders = Integer.parseInt(scanner.nextLine());
        System.out.println("Has Cornering ABS? (yes/no): ");
        boolean hasCorneringABS = scanner.nextLine().equalsIgnoreCase("yes");
        System.out.println("Has Quick Shifter? (yes/no): ");
        boolean hasQuickShifter = scanner.nextLine().equalsIgnoreCase("yes");

        ThermalEngine engine = new ThermalEngine(power, torque, fuelType, fuelConsumption);
        Sport newMotorcycle = new Sport(manufacturer, model, year, price, mileage, engine, engineCapacity, weight,
                hasABS, isA2Compatible, numberOfCylinders, hasCorneringABS, hasQuickShifter);

        try {
            engineService.addEngine(engine);
            vehicleService.addVehicle(newMotorcycle, currentUser.getId());
            clearConsole();
            System.out.println("Sport Motorcycle added successfully!");
        } catch (Exception e) {
            clearConsole();
            System.out.println("Error adding vehicle: " + e.getMessage());
        }
    }

    private static void addNakedMotorcycle() {
        clearConsole();
        System.out.println("Enter Motorcycle Details:\nManufacturer: ");
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
        FuelType fuelType = FuelType.PETROL;
        System.out.println("Fuel Consumption (L/100km): ");
        double fuelConsumption = Double.parseDouble(scanner.nextLine());
        System.out.println("Engine Capacity (cc): ");
        Double engineCapacity = Double.parseDouble(scanner.nextLine());
        System.out.println("Weight (kg): ");
        Double weight = Double.parseDouble(scanner.nextLine());
        System.out.println("Has ABS? (yes/no): ");
        boolean hasABS = scanner.nextLine().equalsIgnoreCase("yes");
        System.out.println("Is A2 Compatible? (yes/no): ");
        boolean isA2Compatible = scanner.nextLine().equalsIgnoreCase("yes");
        System.out.println("Number of Cylinders: ");
        int numberOfCylinders = Integer.parseInt(scanner.nextLine());
        System.out.println("Is Street Fighter? (yes/no): ");
        boolean isStreetFighter = scanner.nextLine().equalsIgnoreCase("yes");
        System.out.println("Riding Modes (comma separated - e.g. SPORT, STREET, RAIN, ECO, CUSTOM): ");
        RidingMode[] ridingModes = parseRidingModes(scanner.nextLine());
        System.out.println("Headlight Type (HALOGEN/LED/XENON): ");
        HeadlightType headlightType = HeadlightType.valueOf(scanner.nextLine().toUpperCase());

        ThermalEngine engine = new ThermalEngine(power, torque, fuelType, fuelConsumption);
        Naked newMotorcycle = new Naked(manufacturer, model, year, price, mileage, engine, engineCapacity, weight,
                hasABS, isA2Compatible, numberOfCylinders, isStreetFighter, ridingModes, headlightType);

        try {
            engineService.addEngine(engine);
            vehicleService.addVehicle(newMotorcycle, currentUser.getId());
            clearConsole();
            System.out.println("Naked Motorcycle added successfully!");
        } catch (Exception e) {
            clearConsole();
            System.out.println("Error adding vehicle: " + e.getMessage());
        }
    }

    private static RidingMode[] parseRidingModes(String nextLine) {
        return List.of(nextLine.split(",")).stream().map(mode -> RidingMode.valueOf(mode.trim().toUpperCase()))
                .toArray(RidingMode[]::new);
    }

    private static void addHybridCar() {
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
        System.out.println("Battery Capacity (kWh): ");
        int batteryCapacity = Integer.parseInt(scanner.nextLine());
        System.out.println("Range (km): ");
        int range = Integer.parseInt(scanner.nextLine());
        System.out.println("Charging Time (hours): ");
        double chargingTime = Double.parseDouble(scanner.nextLine());
        System.out.println("Has Fast Charging? (yes/no): ");
        boolean hasFastCharging = scanner.nextLine().equalsIgnoreCase("yes");
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

        ThermalEngine thermalEngine = new ThermalEngine(power, torque, fuelType, fuelConsumption);
        ElectricEngine electricEngine = new ElectricEngine(power, torque, batteryCapacity, range, chargingTime,
                hasFastCharging);
        HybridEngine engine = new HybridEngine(power, torque, thermalEngine, electricEngine);
        HybridCar newCar = new HybridCar(manufacturer, model, year, price, mileage, engine, doors, bodyType,
                driveType, color);
        try {
            engineService.addEngine(engine);
            vehicleService.addVehicle(newCar, currentUser.getId());
            clearConsole();
            System.out.println("Hybrid Car added successfully!");
        } catch (Exception e) {
            clearConsole();
            System.out.println("Error adding vehicle: " + e.getMessage());
        }
    }

    private static void addElectricCar() {
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
        System.out.println("Battery Capacity (kWh): ");
        int batteryCapacity = Integer.parseInt(scanner.nextLine());
        System.out.println("Range (km): ");
        int range = Integer.parseInt(scanner.nextLine());
        System.out.println("Charging Time (hours): ");
        double chargingTime = Double.parseDouble(scanner.nextLine());
        System.out.println("Has Fast Charging? (yes/no): ");
        boolean hasFastCharging = scanner.nextLine().equalsIgnoreCase("yes");
        System.out.println("Number of Doors: ");
        int doors = Integer.parseInt(scanner.nextLine());
        System.out.println("Body Type (SEDAN/HATCHBACK/SUV): ");
        BodyType bodyType = BodyType.valueOf(scanner.nextLine().toUpperCase());
        System.out.println("Drive Type (FWD/RWD/AWD): ");
        DriveType driveType = DriveType.valueOf(scanner.nextLine().toUpperCase());
        System.out.println("Color: ");
        String color = scanner.nextLine();

        ElectricEngine engine = new ElectricEngine(power, torque, batteryCapacity, range, chargingTime,
                hasFastCharging);
        ElectricCar newCar = new ElectricCar(manufacturer, model, year, price, mileage, engine, doors, bodyType,
                driveType, color);
        try {
            engineService.addEngine(engine);
            vehicleService.addVehicle(newCar, currentUser.getId());
            clearConsole();
            System.out.println("Electric Car added successfully!");
        } catch (Exception e) {
            clearConsole();
            System.out.println("Error adding vehicle: " + e.getMessage());
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

    private static void launchAuction() {
        clearConsole();
        System.out.println("Select a vehicle to auction:\n");
        List<Vehicle> vehicles = vehicleService.getVehiclesByOwnerId(currentUser.getId()).stream()
                .filter(Vehicle::isSellable).toList();
        for (int i = 0; i < vehicles.size(); i++) {
            System.out.println((i + 1) + ". " + vehicles.get(i).getManufacturer() + " " + vehicles.get(i).getModel());
        }
        System.out.println("\n0. Back to Menu | Enter vehicle number to launch auction");
        String choice = scanner.nextLine();
        if (choice.equals("0")) {
            clearConsole();
            return;
        }
        Vehicle selectedVehicle = vehicles.get(Integer.parseInt(choice) - 1);
        System.out.println(
                "Choose auction type:\n1. Buy Now Auction\n2. Blind Auction\n\n0. Back to Menu | Enter option number");
        choice = scanner.nextLine();
        switch (choice) {
            case "1" -> {
                System.out.println("Enter Starting Price (EUR): ");
                double startingPrice = Double.parseDouble(scanner.nextLine());
                System.out.println("Enter Buy Now Price (EUR): ");
                double buyNowPrice = Double.parseDouble(scanner.nextLine());
                System.out.println("Enter Auction Duration (hours): ");
                LocalDateTime endTime = LocalDateTime.now().plusHours(Long.parseLong(scanner.nextLine()));
                try {
                    BuyNowAuction auction = new BuyNowAuction(selectedVehicle, (Seller) currentUser, startingPrice,
                            endTime, buyNowPrice);
                    auctionService.createAuction(auction);
                    selectedVehicle.setSellable(false);
                    clearConsole();
                    System.out.println("Buy Now Auction launched successfully!");
                } catch (Exception e) {
                    clearConsole();
                    System.out.println("Error launching auction: " + e.getMessage());
                }

            }
            case "2" -> {
                System.out.println("Enter Starting Price (EUR): ");
                double startingPrice = Double.parseDouble(scanner.nextLine());
                System.out.println("Enter Auction Duration (hours): ");
                LocalDateTime endTime = LocalDateTime.now().plusHours(Long.parseLong(scanner.nextLine()));
                try {
                    BlindAuction auction = new BlindAuction(selectedVehicle, (Seller) currentUser, startingPrice,
                            endTime);
                    auctionService.createAuction(auction);
                    selectedVehicle.setSellable(false);
                    clearConsole();
                    System.out.println("Blind Auction launched successfully!");
                } catch (Exception e) {
                    clearConsole();
                    System.out.println("Error launching auction: " + e.getMessage());
                }
            }
            case "0" -> {
                clearConsole();
                return;
            }
            default -> {
                clearConsole();
                System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void printAllAuctions() {
        System.out.println("--- Active Auctions ---\n\n");
        List<Auction> activeAuctions = auctionService.getAllAuctions();
        for (int i = 0; i < activeAuctions.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, activeAuctions.get(i).toString());
        }

        System.out.println("\n\n0. Back to Menu | Enter auction number to view details");
        String choice = scanner.nextLine();
        if (choice.equals("0")) {
            clearConsole();
            return;
        } else {
            printAuctionDetails(activeAuctions.get(Integer.parseInt(choice) - 1), false);
        }
    }

    private static void printAuctionDetails(Auction auction, boolean isSeller) {
        clearConsole();
        System.out.println("\n--- Vehicle Details ---");
        System.out.println("Manufacturer: " + auction.getVehicle().getManufacturer());
        System.out.println("Model: " + auction.getVehicle().getModel());
        System.out.println("Year: " + auction.getVehicle().getYear());
        System.out.println("Mileage: " + auction.getVehicle().getMileage() + " km");
        System.out.println("Price: " + auction.getVehicle().getPrice() + " EUR");
        System.out.println("Engine Details: " + auction.getVehicle().getEngine().toString());
        System.out.println("\n--- Auction Details ---");
        System.out.println("Auction Type: " + auction.getAuctionType());
        System.out.println("Starting Price: " + auction.getStartingPrice() + " EUR");
        System.out.println("Current Price: " + auction.getCurrentPrice() + " EUR");
        System.out.println("End Time: " + auction.getEndTime());

        if (!isSeller) {
            System.out.println("\nPress 1 to place bid |Press Enter to go back...");
            String choice = scanner.nextLine();
            if (choice.equals("1")) {
                System.out.println("Enter your bid amount (EUR): ");
                double bidAmount = Double.parseDouble(scanner.nextLine());
                Bid bid = new Bid(currentUser.getId(), bidAmount);
                if (auction.placeBid(bid)) {
                    System.out.println("Bid placed successfully!");
                    return;
                }
            } else {
                clearConsole();
                return;
            }
        } else {
            System.out.println("\nPlaced Bids:");
            for (Bid bid : auction.getBidHistory()) {
                System.out.println("- " + bid.toString());
            }
        }
    }

    private static void viewSellerAuctions(UUID sellerId) {
        clearConsole();
        System.out.println("\n--- My Active Auctions ---");
        List<Auction> auctions = auctionService.getAuctionsBySellerId(sellerId);
        for (int i = 0; i < auctions.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, auctions.get(i).toString());
        }
        System.out.println("\n\n0. Back to Menu | Enter auction number to view details");
        String choice = scanner.nextLine();
        if (choice.equals("0")) {
            clearConsole();
            return;
        } else {
            printAuctionDetails(auctions.get(Integer.parseInt(choice) - 1), true);
        }
    }

    private static void checkBalance() {
        clearConsole();
        System.out.println("\n--- Account Balance ---");
        System.out.println("Current Balance: " + ((Bidder) currentUser).getBalance() + " EUR");
        System.out.println("\nPress 1 to add funds | Press Enter to go back...");
        String choice = scanner.nextLine();
        if (choice.equals("1")) {
            System.out.println("Enter amount to add (EUR): ");
            double amount = Double.parseDouble(scanner.nextLine());
            ((Bidder) currentUser).setBalance(amount);
            System.out.println("Funds added successfully!");
        } else {
            clearConsole();
        }
    }
}
