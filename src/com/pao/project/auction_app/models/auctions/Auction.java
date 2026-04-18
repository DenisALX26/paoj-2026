package com.pao.project.auction_app.models.auctions;

import java.time.LocalDateTime;
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
    protected final List<Bid> bidHistory;

    public Auction(UUID id, Vehicle vehicle, Seller seller, double startingPrice, double currentPrice,
            LocalDateTime endTime, List<Bid> bidHistory) {
        this.id = UUID.randomUUID();
        this.vehicle = vehicle;
        this.seller = seller;
        this.startingPrice = startingPrice;
        this.currentPrice = currentPrice;
        this.endTime = endTime;
        this.bidHistory = bidHistory;
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

    public abstract boolean placeBid(Bid bid);

    @Override
    public String toString() {
        return String.format("Auction %s | Vehicle: %s | Current Price: %.2f EUR", id.toString().substring(0, 8),
                vehicle.getModel(), currentPrice);
    }
}
