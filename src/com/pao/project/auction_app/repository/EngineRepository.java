package com.pao.project.auction_app.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.pao.project.auction_app.models.engines.ElectricEngine;
import com.pao.project.auction_app.models.engines.Engine;
import com.pao.project.auction_app.models.engines.HybridEngine;
import com.pao.project.auction_app.models.engines.ThermalEngine;
import com.pao.project.auction_app.models.engines.enums.FuelType;
import com.pao.project.auction_app.utils.DatabaseConnection;

public class EngineRepository implements Repository<Engine, UUID> {

    @Override
    public void save(Engine entity) {
        String sql = "INSERT INTO engines (id, horsepower, torque, engine_type, fuel_type, fuel_consumption, battery_capacity, range_km, charging_time, has_fast_charging, thermal_engine_id, electric_engine_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, entity.getId().toString());
            pstmt.setInt(2, entity.getHorsepower());
            pstmt.setInt(3, entity.getTorque());
            pstmt.setString(4, entity.getEngineType());

            pstmt.setNull(5, java.sql.Types.VARCHAR);
            pstmt.setNull(6, java.sql.Types.DOUBLE);
            pstmt.setNull(7, java.sql.Types.INTEGER);
            pstmt.setNull(8, java.sql.Types.INTEGER);
            pstmt.setNull(9, java.sql.Types.DOUBLE);
            pstmt.setNull(10, java.sql.Types.BOOLEAN);
            pstmt.setNull(11, java.sql.Types.VARCHAR);
            pstmt.setNull(12, java.sql.Types.VARCHAR);

            if (entity instanceof ThermalEngine) {
                ThermalEngine te = (ThermalEngine) entity;
                pstmt.setString(5, te.getFuelType().name());
                pstmt.setDouble(6, te.getFuelConsumption());
            } else if (entity instanceof ElectricEngine) {
                ElectricEngine ee = (ElectricEngine) entity;
                pstmt.setInt(7, ee.getBatteryCapacity());
                pstmt.setInt(8, ee.getRange());
                pstmt.setDouble(9, ee.getChargingTime());
                pstmt.setBoolean(10, ee.isHasFastCharging());
            } else if (entity instanceof HybridEngine) {
                HybridEngine he = (HybridEngine) entity;
                pstmt.setString(11, he.getThermalEngine().getId().toString());
                pstmt.setString(12, he.getElectricEngine().getId().toString());
            }

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Engine> findById(UUID id) {
        String sql = "SELECT * FROM engines WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEngine(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Engine> findAll() {
        List<Engine> engines = new ArrayList<>();
        String sql = "SELECT * FROM engines";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                engines.add(mapResultSetToEngine(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return engines;
    }

    @Override
    public void update(Engine entity) {
        throw new UnsupportedOperationException("Engine updates not currently supported.");
    }

    @Override
    public void delete(UUID id) {
        String sql = "DELETE FROM engines WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Engine mapResultSetToEngine(ResultSet rs) throws SQLException {
        String type = rs.getString("engine_type");
        int hp = rs.getInt("horsepower");
        int torque = rs.getInt("torque");

        if ("Thermal".equalsIgnoreCase(type)) {
            FuelType fuelType = FuelType.valueOf(rs.getString("fuel_type"));
            double consumption = rs.getDouble("fuel_consumption");
            return new ThermalEngine(hp, torque, fuelType, consumption);
        } else if ("Electric".equalsIgnoreCase(type)) {
            int battery = rs.getInt("battery_capacity");
            int range = rs.getInt("range_km");
            double chargeTime = rs.getDouble("charging_time");
            boolean fastCharge = rs.getBoolean("has_fast_charging");
            return new ElectricEngine(hp, torque, battery, range, chargeTime, fastCharge);
        } else if ("Hybrid".equalsIgnoreCase(type)) {
            return new HybridEngine(hp, torque, null, null);
        }
        throw new SQLException("Unknown engine type: " + type);
    }
}