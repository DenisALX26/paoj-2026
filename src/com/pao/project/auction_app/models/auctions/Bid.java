package com.pao.project.auction_app.models.auctions;

import java.time.LocalDateTime;
import java.util.UUID;

import com.pao.project.auction_app.models.users.Bidder;
import com.pao.project.auction_app.services.UserService;

public final class Bid {
    private final UUID bidderId;
    private final double amount;
    private final LocalDateTime timestamp;
    private final UserService userService = UserService.getInstance();
    
    public Bid(UUID bidderId, double amount) {
        this.bidderId = bidderId;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    public UUID getBidderId() {
        return bidderId;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("%.2f EUR la data %s", amount, timestamp);
    }

    public double getBidderBalance() {
        return ((Bidder) userService.getUserById(bidderId)).getBalance();
    }

    
}
