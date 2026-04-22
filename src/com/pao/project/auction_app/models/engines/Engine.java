package com.pao.project.auction_app.models.engines;

import java.util.UUID;

public abstract class Engine {
    private final UUID id;

    protected final int horsepower, torque;

    public Engine(int horsepower, int torque) {
        this.id = UUID.randomUUID();
        this.horsepower = horsepower;
        this.torque = torque;
    }

    public UUID getId() {
        return id;
    }

    public int getHorsepower() {
        return horsepower;
    }

    public int getTorque() {
        return torque;
    }

    public abstract String getEngineType();

    @Override
    public String toString() {
        return String.format("%s %d HP, %d Nm",getEngineType(), horsepower, torque);
    }

    
}
