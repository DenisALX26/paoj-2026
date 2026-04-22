package com.pao.project.auction_app.models.vehicles.motorcycles;

import com.pao.project.auction_app.models.engines.Engine;

public class Sport extends Motorcycle {
    private final boolean hasCorneringABS, hasQuickShifter;

    public Sport(String manufacturer, String model, int year, int price, int mileage, Engine engine,
            Double engineCapacity, Double weight, boolean hasABS, boolean isA2Compatible, int numberOfCylinders,
            boolean hasCorneringABS, boolean hasQuickShifter) {
        super(manufacturer, model, year, price, mileage, engine, engineCapacity, weight, hasABS, isA2Compatible,
                numberOfCylinders);
        this.hasCorneringABS = hasCorneringABS;
        this.hasQuickShifter = hasQuickShifter;
    }

    public boolean hasCorneringABS() {
        return hasCorneringABS;
    }

    public boolean hasQuickShifter() {
        return hasQuickShifter;
    }
}
