package com.pao.project.auction_app.models.auctions;

import java.time.LocalDateTime;

import com.pao.project.auction_app.exceptions.BidMustBeHigher;
import com.pao.project.auction_app.exceptions.InsuficientFunds;
import com.pao.project.auction_app.models.users.Seller;
import com.pao.project.auction_app.models.vehicles.Vehicle;

public class BuyNowAuction extends Auction {
    private final double buyNowPrice;

    public BuyNowAuction(Vehicle vehicle, Seller seller, double startingPrice,
            LocalDateTime endTime, double buyNowPrice) {
        super(vehicle, seller, startingPrice, endTime);
        this.buyNowPrice = buyNowPrice;
    }

    @Override
    public boolean placeBid(Bid bid) throws BidMustBeHigher, InsuficientFunds {
         if (bid.getAmount() > bid.getBidderBalance()) {
            throw new InsuficientFunds(bid.getAmount(), bid.getBidderBalance());
        }
        if (bid.getAmount() >= buyNowPrice) {
            setCurrentPrice(buyNowPrice);
            bidHistory.add(bid);
            return true;
        } else if (bid.getAmount() > getCurrentPrice()) {
            setCurrentPrice(bid.getAmount());
            bidHistory.add(bid);
            return true;
        } else {
            throw new BidMustBeHigher(bid.getAmount(), getCurrentPrice());
        }
    }

    @Override
    public String getAuctionType() {
        return "Buy Now";
    }
}
