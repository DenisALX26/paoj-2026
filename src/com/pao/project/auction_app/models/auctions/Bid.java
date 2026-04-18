package com.pao.project.auction_app.models.auctions;

import java.time.LocalDateTime;
import java.util.UUID;

public final class Bid {
    private final UUID bidderId;
    private final double amount;
    private final LocalDateTime timestamp;
    
    public Bid(UUID bidderId, double amount, LocalDateTime timestamp) {
        this.bidderId = bidderId;
        this.amount = amount;
        this.timestamp = timestamp;
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

    
}
