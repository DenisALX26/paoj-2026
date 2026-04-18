package com.pao.project.auction_app.models.users;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.pao.project.auction_app.models.vehicles.Vehicle;

public class Seller extends User {
    private final List<Vehicle> vehiclesForSale = new ArrayList<>();

    public Seller(String userName, String email, String password) {
        super(userName, email, password);
    }

    public Seller(UUID id, String userName, String email, String password) {
        super(id, userName, email, password);
    }

    public List<Vehicle> getVehiclesForSale() {
        return vehiclesForSale;
    }

    @Override
    public String getAccountType() {
        return "Seller";
    }

}
