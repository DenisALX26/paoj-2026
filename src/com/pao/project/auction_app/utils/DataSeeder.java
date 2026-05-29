package com.pao.project.auction_app.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.pao.project.auction_app.models.engines.ElectricEngine;
import com.pao.project.auction_app.models.engines.ThermalEngine;
import com.pao.project.auction_app.models.engines.enums.FuelType;
import com.pao.project.auction_app.models.vehicles.cars.ElectricCar;
import com.pao.project.auction_app.models.vehicles.cars.enums.BodyType;
import com.pao.project.auction_app.models.vehicles.cars.enums.DriveType;
import com.pao.project.auction_app.models.vehicles.motorcycles.Naked;
import com.pao.project.auction_app.models.vehicles.motorcycles.Sport;
import com.pao.project.auction_app.models.vehicles.motorcycles.enums.HeadlightType;
import com.pao.project.auction_app.models.vehicles.motorcycles.enums.RidingMode;
import com.pao.project.auction_app.services.EngineService;
import com.pao.project.auction_app.services.UserService;
import com.pao.project.auction_app.services.VehicleService;

public class DataSeeder {
    private static final UserService userService = UserService.getInstance();
    private static final VehicleService vehicleService = VehicleService.getInstance();
    private static final EngineService engineService = EngineService.getInstance();

    public static void seedAll() {
        seedUsers();
        seedVehiclesAndEngines();
    }

    public static void resetData() {
        System.out.println("[SISTEM] Golire baza de date in curs...");
        
        String[] tablesToDelete = {"bids", "auctions", "vehicles", "engines", "users"};

        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            for (String table : tablesToDelete) {
                try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM " + table)) {
                    pstmt.executeUpdate();
                }
            }
            System.out.println("[SISTEM] Toate tabelele au fost golite cu succes!");
        } catch (SQLException e) {
            System.err.println("[Eroare] Resetarea datelor a esuat: " + e.getMessage());
        }
    }

    private static void seedUsers() {
        System.out.println("[SISTEM] Seeding users...");

        try {
            userService.signUp("SELLER", "AutoExpert_RO", "contact@autoexpert.ro", "pass123");
            userService.signUp("SELLER", "VintageCars_Official", "office@vintage.com", "retro99");
            userService.signUp("SELLER", "ElectricMoves", "hello@emoves.ro", "tesla2026");

            userService.signUp("BIDDER", "Ion_Licitatorul", "ion@yahoo.com", "ionut123");
            userService.signUp("BIDDER", "Maria_M", "maria.m@gmail.com", "maria_pass");
            userService.signUp("BIDDER", "Collector_99", "collector@auctions.com", "rare_items");

            System.out.println("[SISTEM] Users seed completat cu succes!");
        } catch (Exception e) {
            System.err.println("Error seeding users: " + e.getMessage());
        }
    }

    private static void seedVehiclesAndEngines() {
        System.out.println("[SISTEM] Seeding engines and vehicles...");
        try {
            ElectricEngine teslaEngine = new ElectricEngine(600, 900, 80, 500, 1.5, true);
            engineService.addEngine(teslaEngine);
            
            ElectricCar tesla = new ElectricCar("Tesla", "Model S", 2020, 100000, 50000,
                    teslaEngine, 4, BodyType.SEDAN, DriveType.AWD, "Black");
            vehicleService.addVehicle(tesla, userService.getUserByEmail("hello@emoves.ro").getId()); // Salvam vehiculul


            ThermalEngine mt07Engine = new ThermalEngine(73, 67, FuelType.PETROL, 4.3);
            engineService.addEngine(mt07Engine);
            
            RidingMode[] mt07Modes = {RidingMode.STREET, RidingMode.ECO};
            Naked mt07 = new Naked("Yamaha", "MT-07", 2023, 7500, 1500, mt07Engine,
                    689.0, 184.0, true, true, 2, true, mt07Modes, HeadlightType.LED);
            vehicleService.addVehicle(mt07, userService.getUserByEmail("contact@autoexpert.ro").getId());


            ThermalEngine r1Engine = new ThermalEngine(200, 113, FuelType.PETROL, 7.2);
            engineService.addEngine(r1Engine);
            
            Sport r1 = new Sport("Yamaha", "R1", 2022, 18000, 5000, r1Engine,
                    998.0, 201.0, true, false, 4, true, true);
            vehicleService.addVehicle(r1, userService.getUserByEmail("office@vintage.com").getId());


            System.out.println("[SISTEM] Vehiculele si motoarele au fost adaugate in baza de date!");
        } catch (Exception e) {
            System.err.println("Error seeding vehicles/engines: " + e.getMessage());
        }
    }
}