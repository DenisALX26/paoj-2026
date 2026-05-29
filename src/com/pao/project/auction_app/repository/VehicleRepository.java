package com.pao.project.auction_app.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.pao.project.auction_app.models.engines.ElectricEngine;
import com.pao.project.auction_app.models.engines.Engine;
import com.pao.project.auction_app.models.vehicles.Vehicle;
import com.pao.project.auction_app.models.vehicles.cars.Car;
import com.pao.project.auction_app.models.vehicles.cars.ElectricCar;
import com.pao.project.auction_app.models.vehicles.cars.HybridCar;
import com.pao.project.auction_app.models.vehicles.cars.ThermalCar;
import com.pao.project.auction_app.models.vehicles.cars.enums.BodyType;
import com.pao.project.auction_app.models.vehicles.cars.enums.DriveType;
import com.pao.project.auction_app.models.vehicles.motorcycles.Motorcycle;
import com.pao.project.auction_app.models.vehicles.motorcycles.Naked;
import com.pao.project.auction_app.models.vehicles.motorcycles.Sport;
import com.pao.project.auction_app.models.vehicles.motorcycles.enums.HeadlightType;
import com.pao.project.auction_app.models.vehicles.motorcycles.enums.RidingMode;
import com.pao.project.auction_app.utils.DatabaseConnection;

public class VehicleRepository implements Repository<Vehicle, UUID> {

    private final EngineRepository engineRepository = new EngineRepository();

    @Override
    public void save(Vehicle entity) {
        String sql = "INSERT INTO vehicles (id, manufacturer, model, production_year, price, mileage, engine_id, is_sellable, " +
                     "vehicle_category, car_type, number_of_doors, body_type, drive_type, color, " +
                     "motorcycle_type, engine_capacity, weight, has_abs, is_a2_compatible, number_of_cylinders, " +
                     "is_street_fighter, riding_modes, headlight_type, has_cornering_abs, has_quick_shifter) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, entity.getId().toString());
            pstmt.setString(2, entity.getManufacturer());
            pstmt.setString(3, entity.getModel());
            pstmt.setInt(4, entity.getYear());
            pstmt.setInt(5, entity.getPrice());
            pstmt.setInt(6, entity.getMileage());
            pstmt.setString(7, entity.getEngine().getId().toString());
            pstmt.setBoolean(8, entity.isSellable());
            pstmt.setString(9, entity.getVehicleCategory());

            for (int i = 10; i <= 25; i++) {
                pstmt.setNull(i, Types.NULL);
            }

            if (entity instanceof Car car) {
                pstmt.setString(10, car.getCarType());
                pstmt.setInt(11, car.getNumberOfDoors());
                pstmt.setString(12, car.getBodyType().name());
                pstmt.setString(13, car.getDriveType().name());
                pstmt.setString(14, car.getColor());
            } else if (entity instanceof Motorcycle moto) {
                pstmt.setDouble(16, moto.getEngineCapacity());
                pstmt.setDouble(17, moto.getWeight());
                pstmt.setBoolean(18, moto.isHasABS());
                pstmt.setBoolean(19, moto.isA2Compatible());
                pstmt.setInt(20, moto.getNumberOfCylinders());

                if (moto instanceof Naked naked) {
                    pstmt.setString(15, "Naked");
                    pstmt.setBoolean(21, naked.isStreetFighter());
                    
                    String modes = Arrays.stream(naked.getRidingModes())
                                         .map(Enum::name)
                                         .collect(Collectors.joining(","));
                    pstmt.setString(22, modes);
                    pstmt.setString(23, naked.getHeadlightType().name());
                } else if (moto instanceof Sport sport) {
                    pstmt.setString(15, "Sport");
                    pstmt.setBoolean(24, sport.hasCorneringABS());
                    pstmt.setBoolean(25, sport.hasQuickShifter());
                }
            }

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Vehicle> findById(UUID id) {
        String sql = "SELECT * FROM vehicles WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, id.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToVehicle(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Vehicle> findAll() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
             
            while (rs.next()) {
                vehicles.add(mapResultSetToVehicle(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    @Override
    public void update(Vehicle entity) {
        String sql = "UPDATE vehicles SET price = ?, mileage = ?, is_sellable = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setInt(1, entity.getPrice());
            pstmt.setInt(2, entity.getMileage());
            pstmt.setBoolean(3, entity.isSellable());
            pstmt.setString(4, entity.getId().toString());
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(UUID id) {
        String sql = "DELETE FROM vehicles WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, id.toString());
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Vehicle mapResultSetToVehicle(ResultSet rs) throws SQLException {
        String category = rs.getString("vehicle_category");
        String manufacturer = rs.getString("manufacturer");
        String model = rs.getString("model");
        int year = rs.getInt("production_year");
        int price = rs.getInt("price");
        int mileage = rs.getInt("mileage");
        boolean isSellable = rs.getBoolean("is_sellable");
        
        UUID engineId = UUID.fromString(rs.getString("engine_id"));
        Engine engine = engineRepository.findById(engineId)
                .orElseThrow(() -> new SQLException("Associated engine not found for ID: " + engineId));

        Vehicle vehicle = null;

        if ("Car".equalsIgnoreCase(category)) {
            String carType = rs.getString("car_type");
            int doors = rs.getInt("number_of_doors");
            BodyType bodyType = BodyType.valueOf(rs.getString("body_type"));
            DriveType driveType = DriveType.valueOf(rs.getString("drive_type"));
            String color = rs.getString("color");

            if ("Thermal".equalsIgnoreCase(carType)) {
                vehicle = new ThermalCar(manufacturer, model, year, price, mileage, engine, doors, bodyType, driveType, color);
            } else if ("Electric".equalsIgnoreCase(carType)) {
                vehicle = new ElectricCar(manufacturer, model, year, price, mileage, (ElectricEngine) engine, doors, bodyType, driveType, color);
            } else if ("Hybrid".equalsIgnoreCase(carType)) {
                vehicle = new HybridCar(manufacturer, model, year, price, mileage, engine, doors, bodyType, driveType, color);
            }
            
        } else if ("Motorcycle".equalsIgnoreCase(category)) {
            String motoType = rs.getString("motorcycle_type");
            Double capacity = rs.getDouble("engine_capacity");
            Double weight = rs.getDouble("weight");
            boolean hasAbs = rs.getBoolean("has_abs");
            boolean isA2 = rs.getBoolean("is_a2_compatible");
            int cylinders = rs.getInt("number_of_cylinders");

            if ("Naked".equalsIgnoreCase(motoType)) {
                boolean streetFighter = rs.getBoolean("is_street_fighter");
                HeadlightType headlight = HeadlightType.valueOf(rs.getString("headlight_type"));
                
                String modesStr = rs.getString("riding_modes");
                RidingMode[] ridingModes = Arrays.stream(modesStr.split(","))
                                                 .map(RidingMode::valueOf)
                                                 .toArray(RidingMode[]::new);
                                                 
                vehicle = new Naked(manufacturer, model, year, price, mileage, engine, capacity, weight, hasAbs, isA2, cylinders, streetFighter, ridingModes, headlight);
            } else if ("Sport".equalsIgnoreCase(motoType)) {
                boolean corneringAbs = rs.getBoolean("has_cornering_abs");
                boolean quickShifter = rs.getBoolean("has_quick_shifter");
                vehicle = new Sport(manufacturer, model, year, price, mileage, engine, capacity, weight, hasAbs, isA2, cylinders, corneringAbs, quickShifter);
            }
        }

        if (vehicle == null) {
            throw new SQLException("Could not map vehicle from database state.");
        }

        vehicle.setSellable(isSellable);
        
        return vehicle;
    }
}