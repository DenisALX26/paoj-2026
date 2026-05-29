package com.pao.project.auction_app.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.pao.project.auction_app.models.auctions.Auction;
import com.pao.project.auction_app.models.auctions.Bid;
import com.pao.project.auction_app.utils.DatabaseConnection;

public class BidService {
    private static BidService instance;
    private final AuditService auditService = AuditService.getInstance();

    private BidService() {}

    public static BidService getInstance() {
        if (instance == null) {
            instance = new BidService();
        }
        return instance;
    }

    public void placeBidTransaction(Auction auction, Bid bid) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        
        String insertBidSql = "INSERT INTO bids (auction_id, bidder_id, amount, bid_timestamp) VALUES (?, ?, ?, ?)";
        String updateAuctionSql = "UPDATE auctions SET current_price = ? WHERE id = ?";

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt1 = conn.prepareStatement(insertBidSql)) {
                pstmt1.setString(1, auction.getId().toString());
                pstmt1.setString(2, bid.getBidderId().toString());
                pstmt1.setDouble(3, bid.getAmount());
                pstmt1.setTimestamp(4, Timestamp.valueOf(bid.getTimestamp()));
                pstmt1.executeUpdate();
            }

            try (PreparedStatement pstmt2 = conn.prepareStatement(updateAuctionSql)) {
                pstmt2.setDouble(1, bid.getAmount());
                pstmt2.setString(2, auction.getId().toString());
                pstmt2.executeUpdate();
            }

            conn.commit();
            
            auditService.logAction("place_bid");

        } catch (SQLException e) {
            System.err.println("Transaction failed. Rolling back changes: " + e.getMessage());
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }
}