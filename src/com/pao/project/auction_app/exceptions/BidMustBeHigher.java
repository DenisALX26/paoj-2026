package com.pao.project.auction_app.exceptions;

public class BidMustBeHigher extends RuntimeException {
    public BidMustBeHigher(Double bidAmount, double currentPrice) {
        super(String.format("Bid amount must be higher than the current price of %.2f EUR. Your bid: %.2f EUR", currentPrice, bidAmount));
    }

}
