package com.pao.project.auction_app.models.vehicles.motorcycles;

import com.pao.project.auction_app.models.engines.Engine;
import com.pao.project.auction_app.models.vehicles.Vehicle;

public abstract class Motorcycle extends Vehicle{
    protected final Double engineCapacity, weight;
    protected final boolean hasABS, isA2Compatible;
    protected final int numberOfCylinders;
    
    public Motorcycle(String manufacturer, String model, int year, int price, int mileage, Engine engine,
            Double engineCapacity, Double weight, boolean hasABS, boolean isA2Compatible, int numberOfCylinders) {
        super(manufacturer, model, year, price, mileage, engine);
        this.engineCapacity = engineCapacity;
        this.weight = weight;
        this.hasABS = hasABS;
        this.isA2Compatible = isA2Compatible;
        this.numberOfCylinders = numberOfCylinders;
    }

    public Double getEngineCapacity() {
        return engineCapacity;
    }

    public Double getWeight() {
        return weight;
    }

    public boolean isHasABS() {
        return hasABS;
    }

    public boolean isA2Compatible() {
        return isA2Compatible;
    }

    public int getNumberOfCylinders() {
        return numberOfCylinders;
    }

    @Override
    public String getVehicleCategory() {
        return "Motorcycle";
    }

    @Override
    public String toString() {
        return String.format("%s %s (%d) - %d EUR, %d km, %.1f cc, %.1f kg, ABS: %s, A2 Compatible: %s, Cylinders: %d",
                getManufacturer(), getModel(), getYear(), getPrice(), getMileage(), engineCapacity, weight, hasABS ? "Yes" : "No", isA2Compatible ? "Yes" : "No", numberOfCylinders);
    }
}
