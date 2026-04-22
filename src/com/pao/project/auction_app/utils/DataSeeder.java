package com.pao.project.auction_app.utils;

import java.io.IOException;
import java.io.PrintWriter;

import com.pao.project.auction_app.models.engines.ElectricEngine;
import com.pao.project.auction_app.models.vehicles.cars.ElectricCar;
import com.pao.project.auction_app.models.vehicles.cars.enums.BodyType;
import com.pao.project.auction_app.models.vehicles.cars.enums.DriveType;
import com.pao.project.auction_app.services.UserService;
import com.pao.project.auction_app.services.VehicleService;

public class DataSeeder {
    private static UserService userService = UserService.getInstance();
    private static VehicleService vehicleService = VehicleService.getInstance();

    public static void seedAll() {
        seedUsers();
        seedVehicles();
    }

    public static void resetData() {
        try {
            new PrintWriter("src/com/pao/project/auction_app/data/users.csv").close();
            new PrintWriter("src/com/pao/project/auction_app/data/vehicles.csv").close();
            System.out.println("Data reset successfully!");
        } catch (IOException e) {
            System.err.println("Error resetting data: " + e.getMessage());
        }
    }

    private static void seedUsers() {
        System.out.println("Seeding users...");

        try {
            userService.signUp("SELLER", "AutoExpert_RO", "contact@autoexpert.ro", "pass123");
            userService.signUp("SELLER", "VintageCars_Official", "office@vintage.com", "retro99");
            userService.signUp("SELLER", "ElectricMoves", "hello@emoves.ro", "tesla2026");

            userService.signUp("BIDDER", "Ion_Licitatorul", "ion@yahoo.com", "ionut123");
            userService.signUp("BIDDER", "Maria_M", "maria.m@gmail.com", "maria_pass");
            userService.signUp("BIDDER", "Collector_99", "collector@auctions.com", "rare_items");

            System.out.println("Seed completat cu succes!");
        } catch (Exception e) {
            System.err.println("Error seeding users: " + e.getMessage());
        }
    }

    private static void seedVehicles() {
        System.out.println("Seeding vehicles...");
        try {
            vehicleService.addVehicle(new ElectricCar("Tesla", "Model S", 2020, 100000, 50000,
                    new ElectricEngine(600, 900, 80, 500, 1.5, true), 4, BodyType.SEDAN, DriveType.AWD, "Black"),
                    userService.getUserByEmail("hello@emoves.ro").getId());
        } catch (Exception e) {
            System.err.println("Error seeding vehicles: " + e.getMessage());
        }
    }

    // TODO: add engines
    private static void seedEnginens() {
        System.out.println("Seeding engines...");
        try {
        } catch (Exception e) {
            System.err.println("Error seeding engines: " + e.getMessage());
        }
    }
}
