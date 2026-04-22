package com.pao.project.auction_app.models.vehicles.cars;

import com.pao.project.auction_app.models.engines.ElectricEngine;
import com.pao.project.auction_app.models.vehicles.cars.enums.BodyType;
import com.pao.project.auction_app.models.vehicles.cars.enums.DriveType;

public class ElectricCar extends Car {
    public ElectricCar(String manufacturer, String model, int year, int price, int mileage, ElectricEngine engine,
            int numberOfDoors, BodyType bodyType, DriveType driveType, String color) {
        super(manufacturer, model, year, price, mileage, engine, numberOfDoors, bodyType, driveType, color);
    }

    @Override
    public String getCarType() {
        return "Electric";
    }

}
