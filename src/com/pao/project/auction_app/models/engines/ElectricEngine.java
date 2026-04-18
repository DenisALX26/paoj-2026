package com.pao.project.auction_app.models.engines;

public class ElectricEngine extends Engine {
    private final int batteryCapacity, range;
    private final Double chargingTime;
    private final boolean hasFastCharging;

    public ElectricEngine(int horsepower, int torque, int batteryCapacity, int range, Double chargingTime,
            boolean hasFastCharging) {
        super(horsepower, torque);
        this.batteryCapacity = batteryCapacity;
        this.range = range;
        this.chargingTime = chargingTime;
        this.hasFastCharging = hasFastCharging;
    }

    public int getBatteryCapacity() {
        return batteryCapacity;
    }

    public int getRange() {
        return range;
    }

    public Double getChargingTime() {
        return chargingTime;
    }

    public boolean isHasFastCharging() {
        return hasFastCharging;
    }

    @Override
    public String getEngineType() {
        return "Electric";
    }
}
