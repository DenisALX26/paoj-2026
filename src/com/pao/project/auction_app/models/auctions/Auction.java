package com.pao.project.auction_app.models.auctions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.pao.project.auction_app.models.users.Seller;
import com.pao.project.auction_app.models.vehicles.Vehicle;

public abstract class Auction {
    private final UUID id;
    protected final Vehicle vehicle;
    protected final Seller seller;
    protected final double startingPrice;
    protected double currentPrice;
    protected LocalDateTime endTime;
    protected final List<Bid> bidHistory = new ArrayList<>();

    public Auction(Vehicle vehicle, Seller seller, double startingPrice,
            LocalDateTime endTime) {
        this.id = UUID.randomUUID();
        this.vehicle = vehicle;
        this.seller = seller;
        this.startingPrice = startingPrice;
        this.endTime = endTime;
        this.currentPrice = startingPrice;
    }

    public UUID getId() {
        return id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Seller getSeller() {
        return seller;
    }

    public double getStartingPrice() {
        return startingPrice;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public List<Bid> getBidHistory() {
        return bidHistory;
    }

    public abstract String getAuctionType();

    public abstract boolean placeBid(Bid bid);

    @Override
    public String toString() {
        return String.format("Vehicle: %s %s | Current Price: %.2f EUR | Remaining Time: %s", vehicle.getManufacturer(), vehicle.getModel(), currentPrice, endTime.isAfter(LocalDateTime.now()) ? java.time.Duration.between(LocalDateTime.now(), endTime).toHours() + " hours" : "Auction Ended");
    }
}
