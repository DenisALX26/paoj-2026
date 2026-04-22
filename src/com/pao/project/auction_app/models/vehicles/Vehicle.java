package com.pao.project.auction_app.models.vehicles;

import java.util.UUID;

import com.pao.project.auction_app.models.engines.Engine;

public abstract class Vehicle {
    private final UUID id;

    protected final String manufacturer, model;
    protected final int year;
    protected int price, mileage;
    protected final Engine engine;
    protected boolean isSellable = true;

    public Vehicle(String manufacturer, String model, int year, int price, int mileage, Engine engine) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.year = year;
        this.engine = engine;
        this.price = price;
        this.mileage = mileage;
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public boolean isSellable() {
        return isSellable;
    }

    public abstract String getVehicleCategory();

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof Vehicle && this.id.equals(((Vehicle) obj).id));
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s %s %d - $%d, %d miles, %s", manufacturer, model, year, price, mileage, engine.toString());
    }

    public Engine getEngine() {
        return engine;
    }

    public void setSellable(boolean isSellable) {
        this.isSellable = isSellable;
    }

}
