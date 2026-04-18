package com.pao.project.auction_app.models.users;

import java.util.List;
import java.util.UUID;

import com.pao.project.auction_app.models.vehicles.Vehicle;

public class Seller extends User {
    private final List<Vehicle> vehiclesForSale;

    public Seller(UUID id, String userName, String email, String password, List<Vehicle> vehiclesForSale) {
        super(id, userName, email, password);
        this.vehiclesForSale = vehiclesForSale;
    }

    public List<Vehicle> getVehiclesForSale() {
        return vehiclesForSale;
    }

    @Override
    public String getAccountType() {
        return "Seller";
    }

}
