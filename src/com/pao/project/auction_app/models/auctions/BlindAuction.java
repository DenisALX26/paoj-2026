package com.pao.project.auction_app.models.auctions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.pao.project.auction_app.models.users.Seller;
import com.pao.project.auction_app.models.vehicles.Vehicle;

public class BlindAuction extends Auction{
    public BlindAuction(UUID id, Vehicle vehicle, Seller seller, double startingPrice, double currentPrice,
            LocalDateTime endTime, List<Bid> bidHistory) {
        super(id, vehicle, seller, startingPrice, currentPrice, endTime, bidHistory);
    }

    @Override
    public boolean placeBid(Bid bid) {
        bidHistory.add(bid);
        return true;
    }

    
    
}
