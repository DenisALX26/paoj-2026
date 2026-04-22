package com.pao.project.auction_app.models.users;

import java.util.UUID;

public class Bidder extends User{
    private double balance = 0.0;

    public Bidder(String userName, String email, String password) {
        super(userName, email, password);
    }

    public Bidder(UUID id, String userName, String email, String password, double balance) {
        super(id, userName, email, password);
        this.balance += balance;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance += balance;
    }

    @Override
    public String getAccountType() {
        return "Bidder";
    }

    
    
}
