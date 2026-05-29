package com.pao.project.auction_app.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.pao.project.auction_app.utils.DatabaseConnection;

public class ReportService {
    private static ReportService instance;
    private final AuditService auditService = AuditService.getInstance();

    private ReportService() {
    }

    public static ReportService getInstance() {
        if (instance == null) {
            instance = new ReportService();
        }
        return instance;
    }

    public record AuctionSummary(String vehicleInfo, String sellerName, double currentPrice, String auctionType) {
    }

    public record BidDetail(String bidderName, String email, double amount, LocalDateTime timestamp) {
    }

    public record TopBid(String bidderName, String vehicleInfo, double amount) {
    }

    public List<AuctionSummary> getActiveAuctionsSummary() {
        List<AuctionSummary> summaries = new ArrayList<>();
        String sql = "SELECT v.manufacturer, v.model, u.username AS seller_name, a.current_price, a.auction_type " +
                "FROM auctions a " +
                "JOIN vehicles v ON a.vehicle_id = v.id " +
                "JOIN users u ON a.seller_id = u.id " +
                "WHERE a.end_time > NOW() " +
                "ORDER BY a.current_price DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String vehicleInfo = rs.getString("manufacturer") + " " + rs.getString("model");
                String sellerName = rs.getString("seller_name");
                double currentPrice = rs.getDouble("current_price");
                String auctionType = rs.getString("auction_type");

                summaries.add(new AuctionSummary(vehicleInfo, sellerName, currentPrice, auctionType));
            }
            auditService.logAction("report_active_auctions");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return summaries;
    }

    public List<BidDetail> getDetailedBidHistory(UUID auctionId) {
        List<BidDetail> history = new ArrayList<>();
        String sql = "SELECT u.username, u.email, b.amount, b.bid_timestamp " +
                "FROM bids b " +
                "JOIN users u ON b.bidder_id = u.id " +
                "WHERE b.auction_id = ? " +
                "ORDER BY b.amount DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, auctionId.toString());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String username = rs.getString("username");
                    String email = rs.getString("email");
                    double amount = rs.getDouble("amount");
                    LocalDateTime timestamp = rs.getTimestamp("bid_timestamp").toLocalDateTime();

                    history.add(new BidDetail(username, email, amount, timestamp));
                }
            }
            auditService.logAction("report_auction_bid_history");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return history;
    }

    public List<TopBid> getTopHighestBids(int limit) {
        List<TopBid> topBids = new ArrayList<>();
        String sql = "SELECT u.username, v.manufacturer, v.model, b.amount " +
                "FROM bids b " +
                "JOIN users u ON b.bidder_id = u.id " +
                "JOIN auctions a ON b.auction_id = a.id " +
                "JOIN vehicles v ON a.vehicle_id = v.id " +
                "ORDER BY b.amount DESC LIMIT ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, limit);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String username = rs.getString("username");
                    String vehicleInfo = rs.getString("manufacturer") + " " + rs.getString("model");
                    double amount = rs.getDouble("amount");

                    topBids.add(new TopBid(username, vehicleInfo, amount));
                }
            }
            auditService.logAction("report_top_highest_bids");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topBids;
    }
}