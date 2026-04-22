package com.pao.project.auction_app.models.vehicles.motorcycles;

import com.pao.project.auction_app.models.engines.Engine;
import com.pao.project.auction_app.models.vehicles.motorcycles.enums.HeadlightType;
import com.pao.project.auction_app.models.vehicles.motorcycles.enums.RidingMode;

public class Naked extends Motorcycle {
    private final boolean isStreetFighter;
    private final RidingMode ridingModes[];
    private final HeadlightType headlightType;

    public Naked(String manufacturer, String model, int year, int price, int mileage, Engine engine,
            Double engineCapacity, Double weight, boolean hasABS, boolean isA2Compatible, int numberOfCylinders,
            boolean isStreetFighter, RidingMode[] ridingModes, HeadlightType headlightType) {
        super(manufacturer, model, year, price, mileage, engine, engineCapacity, weight, hasABS, isA2Compatible,
                numberOfCylinders);
        this.isStreetFighter = isStreetFighter;
        this.ridingModes = ridingModes;
        this.headlightType = headlightType;
    }

    public boolean isStreetFighter() {
        return isStreetFighter;
    }

    public RidingMode[] getRidingModes() {
        return ridingModes;
    }

    public HeadlightType getHeadlightType() {
        return headlightType;
    }
}
