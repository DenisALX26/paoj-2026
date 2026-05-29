package com.pao.project.auction_app.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.pao.project.auction_app.models.vehicles.Vehicle;
import com.pao.project.auction_app.repository.VehicleRepository;

public class VehicleService {
    private static VehicleService instance;
    private final VehicleRepository vehicleRepository = new VehicleRepository();
    
    private final List<Vehicle> allVehicles = new ArrayList<>();
    private final Map<UUID, List<Vehicle>> allVehiclesGroupedByOwnerId = new HashMap<>();

    private VehicleService() {
    }

    public static VehicleService getInstance() {
        if (instance == null) {
            instance = new VehicleService();
        }
        return instance;
    }

    public void addVehicle(Vehicle vehicle, UUID ownerId) {
        System.out.println(vehicle.toString());

        allVehicles.add(vehicle);
        allVehiclesGroupedByOwnerId.computeIfAbsent(ownerId, k -> new ArrayList<>()).add(vehicle);

        vehicleRepository.save(vehicle);
    }

    public void removeVehicle(UUID id) {
        allVehicles.removeIf(vehicle -> vehicle.getId().equals(id));
        vehicleRepository.delete(id);
    }

    public Vehicle getVehicleById(UUID id) {
        return vehicleRepository.findById(id).orElse(null);
    }

    public List<Vehicle> getAllVehiclesSortedByPrice() {
        List<Vehicle> sortedVehicles = new ArrayList<>(allVehicles);
        sortedVehicles.sort(Comparator.comparingInt(Vehicle::getPrice));
        return sortedVehicles;
    }

    public List<Vehicle> getAllVehicles() {
        return new ArrayList<>(allVehicles);
    }

    public List<Vehicle> getVehiclesByOwnerId(UUID ownerId) {
        return allVehiclesGroupedByOwnerId.getOrDefault(ownerId, new ArrayList<>());
    }
}