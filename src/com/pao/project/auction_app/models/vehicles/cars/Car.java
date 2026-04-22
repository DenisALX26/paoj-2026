package com.pao.project.auction_app.models.vehicles.cars;

import com.pao.project.auction_app.models.engines.Engine;
import com.pao.project.auction_app.models.vehicles.Vehicle;
import com.pao.project.auction_app.models.vehicles.cars.enums.BodyType;
import com.pao.project.auction_app.models.vehicles.cars.enums.DriveType;

public abstract class Car extends Vehicle{
    protected final int numberOfDoors;
    protected final BodyType bodyType;
    protected final DriveType driveType;
    protected final String color;
    
    public Car(String manufacturer, String model, int year, int price, int mileage, Engine engine, int numberOfDoors, BodyType bodyType, DriveType driveType, String color) {
        super(manufacturer, model, year, price, mileage, engine);
        this.numberOfDoors = numberOfDoors;
        this.bodyType = bodyType;
        this.driveType = driveType;
        this.color = color;
    }

    public int getNumberOfDoors() {
        return numberOfDoors;
    }

    public BodyType getBodyType() {
        return bodyType;
    }

    public DriveType getDriveType() {
        return driveType;
    }

    public String getColor() {
        return color;
    }

    public abstract String getCarType();

    @Override
    public String getVehicleCategory() {
        return "Car";
    }

    @Override
    public String toString() {
        return String.format("%s %s %s (%d) - %d EUR, %d km, %s, %s, %s, %d doors",
                getManufacturer(), getModel(), getColor(), getYear(), getPrice(), getMileage(),
                bodyType, driveType, engine.getEngineType(), numberOfDoors);
    }

    
}
