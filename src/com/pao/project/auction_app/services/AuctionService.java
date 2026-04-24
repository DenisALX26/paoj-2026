package com.pao.project.auction_app.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import com.pao.project.auction_app.models.auctions.Auction;

public class AuctionService {
    private static AuctionService instance;
    private final Map<UUID, Auction> auctions = new HashMap<>();
    private final Set<Auction> sortedAuctions = new TreeSet<>();
    

    private AuctionService() {
    }

    public static AuctionService getInstance() {
        if (instance == null) {
            instance = new AuctionService();
        }
        return instance;
    }

    public void createAuction(Auction auction) {
        auctions.put(auction.getId(), auction);
        sortedAuctions.add(auction);
    }

    public List<Auction> getAllAuctions() {
        return new ArrayList<>(sortedAuctions);
    }

    public List<Auction> getAuctionsBySellerId(UUID sellerId) {
        List<Auction> result = new ArrayList<>();
        for (Auction auction : auctions.values()) {
            if (auction.getSeller().getId().equals(sellerId)) {
                result.add(auction);
            }
        }
        return result;
    }
}
