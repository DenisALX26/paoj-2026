package com.pao.project.auction_app.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.pao.project.auction_app.models.auctions.Auction;
import com.pao.project.auction_app.models.auctions.BlindAuction;
import com.pao.project.auction_app.models.auctions.BuyNowAuction;
import com.pao.project.auction_app.utils.DatabaseConnection;

public class AuctionRepository implements Repository<Auction, UUID> {
    @Override
    public void save(Auction entity) {
        String sql = "INSERT INTO auctions (id, vehicle_id, seller_id, starting_price, current_price, end_time, auction_type, buy_now_price) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entity.getId().toString());
            stmt.setString(2, entity.getVehicle().getId().toString());
            stmt.setString(3, entity.getSeller().getId().toString());
            stmt.setDouble(4, entity.getStartingPrice());
            stmt.setDouble(5, entity.getCurrentPrice());
            stmt.setTimestamp(6, Timestamp.valueOf(entity.getEndTime()));
            stmt.setString(7, entity.getAuctionType());

            if (entity instanceof BuyNowAuction) {
                stmt.setDouble(8, ((BuyNowAuction) entity).getBuyNowPrice());
            } else {
                stmt.setNull(8, java.sql.Types.DOUBLE);
            }

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Auction> findById(UUID id) {
        String sql = "SELECT * FROM auctions WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToAuction(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Auction> findAll() {
        List<Auction> auctions = new ArrayList<>();
        String sql = "SELECT * FROM auctions";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                auctions.add(mapResultSetToAuction(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return auctions;
    }

    @Override
    public void update(Auction entity) {
        String sql = "UPDATE auctions SET current_price = ?, end_time = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, entity.getCurrentPrice());
            pstmt.setTimestamp(2, Timestamp.valueOf(entity.getEndTime()));
            pstmt.setString(3, entity.getId().toString());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(UUID id) {
        String sql = "DELETE FROM auctions WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id.toString());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Auction mapResultSetToAuction(ResultSet rs) throws SQLException {
        UUID id = UUID.fromString(rs.getString("id"));
        double startingPrice = rs.getDouble("starting_price");
        double currentPrice = rs.getDouble("current_price");
        java.time.LocalDateTime endTime = rs.getTimestamp("end_time").toLocalDateTime();
        String auctionType = rs.getString("auction_type");

        if ("Buy Now".equalsIgnoreCase(auctionType)) {
            double buyNowPrice = rs.getDouble("buy_now_price");
            BuyNowAuction auction = new BuyNowAuction(null, null, startingPrice, endTime, buyNowPrice);
            auction.setCurrentPrice(currentPrice);
            return auction;
        } else {
            BlindAuction auction = new BlindAuction(null, null, startingPrice, endTime);
            auction.setCurrentPrice(currentPrice);
            return auction;
        }
    }
}
