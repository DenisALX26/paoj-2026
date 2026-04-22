package com.pao.project.auction_app.models.vehicles.cars;

import com.pao.project.auction_app.models.engines.Engine;
import com.pao.project.auction_app.models.vehicles.cars.enums.BodyType;
import com.pao.project.auction_app.models.vehicles.cars.enums.DriveType;

public class ThermalCar extends Car {
    public ThermalCar(String manufacturer, String model, int year, int price, int mileage, Engine engine,
            int numberOfDoors, BodyType bodyType, DriveType driveType, String color) {
        super(manufacturer, model, year, price, mileage, engine, numberOfDoors, bodyType, driveType, color);
    }

    @Override
    public String getCarType() {
        return "Thermal";
    }
}
