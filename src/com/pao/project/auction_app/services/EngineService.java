package com.pao.project.auction_app.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.pao.project.auction_app.models.engines.ElectricEngine;
import com.pao.project.auction_app.models.engines.Engine;
import com.pao.project.auction_app.models.engines.HybridEngine;
import com.pao.project.auction_app.models.engines.ThermalEngine;

public class EngineService {
    private static EngineService instance;
    private final List<Engine> allEngines = new ArrayList<>();
    private final String FILE_PATH = "src/com/pao/project/auction_app/data/engines.csv";

    private EngineService() {
    }

    public static EngineService getInstance() {
        if (instance == null) {
            instance = new EngineService();
        }
        return instance;
    }

    public Engine getEngineById(UUID id) {
        return allEngines.stream()
                .filter(engine -> engine.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void addEngine(Engine engine) {
        allEngines.add(engine);
        saveEngineToFile(engine);
    }

    private void saveEngineToFile(Engine engine) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(FILE_PATH, true)))) {
            String csvOutput = String.format("%s|%s|%d|%d", engine.getId(), engine.getEngineType(),
                    engine.getHorsepower(), engine.getTorque());

            if (engine instanceof ElectricEngine electricEngine) {
                csvOutput += String.format("|%d|%d|%.1f|%s", electricEngine.getBatteryCapacity(),
                        electricEngine.getRange(),
                        electricEngine.getChargingTime(), electricEngine.isHasFastCharging() ? "Yes" : "No");
            } else if (engine instanceof ThermalEngine thermalEngine) {
                csvOutput += String.format("|%s|%.1f", thermalEngine.getFuelType(), thermalEngine.getFuelConsumption());
            } else if (engine instanceof HybridEngine hybridEngine) {
                csvOutput += String.format("|%.1f|%d|%d", hybridEngine.getThermalEngine().getFuelConsumption(),
                        hybridEngine.getElectricEngine().getBatteryCapacity(), hybridEngine.getElectricEngine().getRange());
            }
            out.println(csvOutput);
        } catch (Exception e) {
            System.err.println("Error saving engine: " + e.getMessage());
        }
    }
}
