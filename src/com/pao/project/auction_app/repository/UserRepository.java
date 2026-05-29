package com.pao.project.auction_app.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.pao.project.auction_app.models.users.Bidder;
import com.pao.project.auction_app.models.users.Seller;
import com.pao.project.auction_app.models.users.User;
import com.pao.project.auction_app.utils.DatabaseConnection;

public class UserRepository implements Repository<User, UUID> {

    @Override
    public void save(User entity) {
        String sql = "INSERT INTO users (id, username, email, password, account_type, balance) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, entity.getId().toString());
            pstmt.setString(2, entity.getUserName());
            pstmt.setString(3, entity.getEmail());
            pstmt.setString(4, entity.getPassword());
            pstmt.setString(5, entity.getAccountType());

            if (entity instanceof Bidder) {
                pstmt.setDouble(6, ((Bidder) entity).getBalance());
            } else {
                pstmt.setDouble(6, 0.0);
            }

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<User> findById(UUID id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id.toString());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void update(User entity) {
        String sql = "UPDATE users SET username = ?, email = ?, password = ?, balance = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, entity.getUserName());
            pstmt.setString(2, entity.getEmail());
            pstmt.setString(3, entity.getPassword());

            if (entity instanceof Bidder) {
                pstmt.setDouble(4, ((Bidder) entity).getBalance());
            } else {
                pstmt.setDouble(4, 0.0);
            }
            
            pstmt.setString(5, entity.getId().toString());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(UUID id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id.toString());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        UUID id = UUID.fromString(rs.getString("id"));
        String username = rs.getString("username");
        String email = rs.getString("email");
        String password = rs.getString("password");
        String accountType = rs.getString("account_type");

        if ("Bidder".equalsIgnoreCase(accountType)) {
            double balance = rs.getDouble("balance");
            return new Bidder(id, username, email, password, balance);
        } else {
            return new Seller(id, username, email, password);
        }
    }
}