package com.pao.project.auction_app.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.pao.project.auction_app.models.vehicles.Vehicle;

public class VehicleService {
    private static VehicleService instance;
    private final Map<UUID, Vehicle> vehicles = new HashMap<>();

    private VehicleService() {}

    public static VehicleService getInstance() {
        if (instance == null) {
            instance = new VehicleService();
        }
        return instance;
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.put(vehicle.getId(), vehicle);
    }

    public void removeVehicle(UUID id) {
        vehicles.remove(id);
    }

    public Vehicle getVehicleById(UUID id) {
        return vehicles.get(id);
    }

    public List<Vehicle> getAllVehiclesSortedByPrice() {
        List<Vehicle> sortedVehicles = new ArrayList<>(vehicles.values());
        sortedVehicles.sort(Comparator.comparingInt(Vehicle::getPrice));
        return sortedVehicles;
    }

    public List<Vehicle> getAllVehicles() {
        return new ArrayList<>(vehicles.values());
    }
}
