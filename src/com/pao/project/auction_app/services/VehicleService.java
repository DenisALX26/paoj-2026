package com.pao.project.auction_app.services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import com.pao.project.auction_app.models.engines.ElectricEngine;
import com.pao.project.auction_app.models.engines.Engine;
import com.pao.project.auction_app.models.vehicles.Vehicle;
import com.pao.project.auction_app.models.vehicles.cars.Car;
import com.pao.project.auction_app.models.vehicles.cars.ElectricCar;
import com.pao.project.auction_app.models.vehicles.cars.enums.*;
import com.pao.project.auction_app.models.vehicles.motorcycles.Motorcycle;

public class VehicleService {
    private static VehicleService instance;
    private final List<Vehicle> allVehicles = new ArrayList<>();
    private final Map<UUID, List<Vehicle>> allVehiclesGroupedByOwnerId = new HashMap<>();
    private final String FILE_PATH = "src/com/pao/project/auction_app/data/vehicles.csv";
    private final EngineService engineService = EngineService.getInstance();

    private VehicleService() {
        loadVehiclesFromFile();
    }

    public static VehicleService getInstance() {
        if (instance == null) {
            instance = new VehicleService();
        }
        return instance;
    }

    private void saveVehicleToFile(Vehicle vehicle, UUID ownerId) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(FILE_PATH, true)))) {
            String csvOutput = String.format("%s|%s|%d|%d|%d|%s", vehicle.getManufacturer(), vehicle.getModel(),
                    vehicle.getYear(), vehicle.getMileage(), vehicle.getPrice(), vehicle.getEngine().getId());
            if (vehicle instanceof Car car) {
                csvOutput = "Car|" + car.getCarType() + "|" + csvOutput + "|" + car.getNumberOfDoors() + "|"
                        + car.getBodyType() + "|" + car.getDriveType() + "|" + car.getColor();
            } else if (vehicle instanceof Motorcycle motorcycle) {
                csvOutput = "Motorcycle|" + motorcycle.getClass().getSimpleName() + "|" + csvOutput;
            }

            out.println(csvOutput);
        } catch (IOException e) {
            System.err.println("Error saving vehicle: " + e.getMessage());
        }
    }

    public void addVehicle(Vehicle vehicle, UUID ownerId) throws InterruptedException {
        System.out.println(vehicle.toString());

        allVehicles.add(vehicle);
        allVehiclesGroupedByOwnerId.computeIfAbsent(ownerId, k -> new ArrayList<>()).add(vehicle);

        saveVehicleToFile(vehicle, ownerId);
    }

    public void removeVehicle(UUID id) {
        allVehicles.removeIf(vehicle -> vehicle.getId().equals(id));
    }

    public Vehicle getVehicleById(UUID id) {
        return allVehicles.stream()
                .filter(vehicle -> vehicle.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Vehicle> getAllVehiclesSortedByPrice() {
        List<Vehicle> sortedVehicles = new ArrayList<>(allVehicles);
        sortedVehicles.sort(Comparator.comparingInt(Vehicle::getPrice));
        return sortedVehicles;
    }

    public List<Vehicle> getAllVehicles() {
        return new ArrayList<>(allVehicles);
    }

    // TODO: nu este terminata
    private void loadCarFromFile(String vehicleType, String subType, String manufacturer, String model, int year,
            int mileage, int price, UUID engineId, int numberOfDoors, BodyType bodyType, DriveType driveType,
            String color) {
        try {
            switch (subType) {
                case "Electric": {
                    Engine engine = engineService.getEngineById(engineId);
                    ElectricEngine electricEngine = new ElectricEngine(engine.getHorsepower(), engine.getTorque(), price, numberOfDoors, null,
                            false);

                    ElectricCar car = new ElectricCar(manufacturer, model, year, price, mileage,
                            electricEngine,
                            numberOfDoors, bodyType, driveType, color);

                    allVehicles.add(car);
                    allVehiclesGroupedByOwnerId.computeIfAbsent(UUID.fromString("e789ef55-6375-40db-adb2-53966c155a47"), k -> new ArrayList<>()).add(car);
                    break;
                }
                default:
                    break;
            }

        } catch (Exception e) {
            System.err.println("Error loading car from file: " + e.getMessage());
        }
    }

    private void loadVehiclesFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\|");

                String vehicleType = parts[0], subType = parts[1], manufacturer = parts[2], model = parts[3];
                int year = Integer.parseInt(parts[4]), mileage = Integer.parseInt(parts[5]),
                        price = Integer.parseInt(parts[6]);
                UUID engineId = UUID.fromString(parts[7]);

                switch (vehicleType) {
                    case "Car" -> {
                        int numberOfDoors = Integer.parseInt(parts[8]);
                        BodyType bodyType = BodyType.valueOf(parts[9]);
                        DriveType driveType = DriveType.valueOf(parts[10]);
                        String color = parts[11];
                        loadCarFromFile(vehicleType, subType, manufacturer, model, year, mileage, price, engineId,
                                numberOfDoors, bodyType, driveType, color);
                    }
                    case "Motorcycle" -> loadMotorcycleFromFile(vehicleType, subType, manufacturer, model, year,
                            mileage, price, engineId);
                    default -> System.err.println("Unknown vehicle type in file: " + vehicleType);
                }

            }
        } catch (IOException e) {
            System.err.println("Error loading vehicles from file: " + e.getMessage());
        }
    }

    private void loadMotorcycleFromFile(String vehicleType, String subType, String manufacturer, String model,
            int year, int mileage, int price, UUID engineId) {
    }

    public List<Vehicle> getVehiclesByOwnerId(UUID ownerId) {
        return allVehiclesGroupedByOwnerId.getOrDefault(ownerId, new ArrayList<>());
    }
}
