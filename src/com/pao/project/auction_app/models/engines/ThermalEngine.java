package com.pao.project.auction_app.models.engines;

import com.pao.project.auction_app.models.engines.enums.FuelType;

public class ThermalEngine extends Engine {
    private final FuelType fuelType;
    private final Double fuelConsumption;

    public ThermalEngine(int horsepower, int torque, FuelType fuelType, Double fuelConsumption) {
        super(horsepower, torque);
        this.fuelType = fuelType;
        this.fuelConsumption = fuelConsumption;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public Double getFuelConsumption() {
        return fuelConsumption;
    }

    @Override
    public String getEngineType() {
        return "Thermal";
    }
}
