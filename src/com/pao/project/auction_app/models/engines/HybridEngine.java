package com.pao.project.auction_app.models.engines;

public class HybridEngine extends Engine{
    private final ThermalEngine thermalEngine;
    private final ElectricEngine electricEngine;

    public HybridEngine(int horsepower, int torque, ThermalEngine thermalEngine, ElectricEngine electricEngine) {
        super(horsepower, torque);
        this.thermalEngine = thermalEngine;
        this.electricEngine = electricEngine;
    }

    public ThermalEngine getThermalEngine() {
        return thermalEngine;
    }

    public ElectricEngine getElectricEngine() {
        return electricEngine;
    }

    @Override
    public String getEngineType() {
        return "Hybrid";
    }
}
