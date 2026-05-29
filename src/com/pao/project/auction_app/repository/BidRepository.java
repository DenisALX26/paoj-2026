package com.pao.project.auction_app.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.pao.project.auction_app.models.auctions.Bid;
import com.pao.project.auction_app.utils.DatabaseConnection;

public class BidRepository implements Repository<Bid, Integer> {

    @Override
    public void save(Bid entity) {
        throw new UnsupportedOperationException("Bids should be saved using BidService.placeBidTransaction()");
    }

    public List<Bid> findBidsByAuctionId(UUID auctionId) {
        List<Bid> bids = new ArrayList<>();
        String sql = "SELECT * FROM bids WHERE auction_id = ? ORDER BY bid_timestamp ASC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, auctionId.toString());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bids.add(mapResultSetToBid(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bids;
    }

    @Override
    public Optional<Bid> findById(Integer id) {
        String sql = "SELECT * FROM bids WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToBid(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Bid> findAll() {
        List<Bid> bids = new ArrayList<>();
        String sql = "SELECT * FROM bids ORDER BY bid_timestamp DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                bids.add(mapResultSetToBid(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bids;
    }

    @Override
    public void update(Bid entity) {
        throw new UnsupportedOperationException("Bids are immutable and cannot be updated once placed.");
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM bids WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Bid mapResultSetToBid(ResultSet rs) throws SQLException {
        UUID bidderId = UUID.fromString(rs.getString("bidder_id"));
        double amount = rs.getDouble("amount");
        return new Bid(bidderId, amount); 
    }
}