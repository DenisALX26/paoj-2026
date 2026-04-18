package com.pao.project.auction_app.models.auctions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.pao.project.auction_app.models.users.Seller;
import com.pao.project.auction_app.models.vehicles.Vehicle;

public class BuyNowAuction extends Auction{
    private final double buyNowPrice;

    public BuyNowAuction(UUID id, Vehicle vehicle, Seller seller, double startingPrice, double currentPrice,
            LocalDateTime endTime, List<Bid> bidHistory, double buyNowPrice) {
        super(id, vehicle, seller, startingPrice, currentPrice, endTime, bidHistory);
        this.buyNowPrice = buyNowPrice;
    }

    @Override
    public boolean placeBid(Bid bid) {
        if(bid.getAmount() <= currentPrice) return false;

        bidHistory.add(bid);
        currentPrice = bid.getAmount();

        if (currentPrice >= buyNowPrice) {
            endTime = LocalDateTime.now();
        }
        return true;
    }

    
}
