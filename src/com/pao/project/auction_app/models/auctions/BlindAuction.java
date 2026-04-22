package com.pao.project.auction_app.models.auctions;

import java.time.LocalDateTime;
import com.pao.project.auction_app.models.users.Seller;
import com.pao.project.auction_app.models.vehicles.Vehicle;

public class BlindAuction extends Auction{
    public BlindAuction(Vehicle vehicle, Seller seller, double startingPrice, LocalDateTime endTime) {
        super(vehicle, seller, startingPrice, endTime);
    }

    @Override
    public boolean placeBid(Bid bid) {
        bidHistory.add(bid);
        return true;
    }

    @Override
    public String getAuctionType() {
        return "Blind";
    }

    
    
}
