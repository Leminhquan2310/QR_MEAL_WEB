package com.qr_meal_web.repository.impl;

import com.qr_meal_web.enums.DiscountStatus;
import com.qr_meal_web.enums.DiscountType;
import com.qr_meal_web.model.Discount;
import com.qr_meal_web.repository.DiscountRepository;
import com.qr_meal_web.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DiscountRepositoryImpl implements DiscountRepository {
    private final String SELECT_ALL = "SELECT * FROM discount LIMIT ? OFFSET ?";
    private final String SELECT_ONE = "SELECT * FROM discount WHERE id=?";
    private final String CREATE = "INSERT INTO discount(points_required, description, discount_value, discount_type) VALUES (?, ?, ?, ?)";
    private final String UPDATE = "UPDATE discount SET points_required=?, description=?, discount_value = ?,  discount_type = ?, status = ? WHERE id=?";
    private final String DELETE = "UPDATE discount SET status = 0 WHERE id=?";
    private final String SEARCH_COUNT_TOTAL = "SELECT count(*) AS count_total FROM discount";

    @Override
    public List<Discount> getAll(int limit, int offset) {
        List<Discount> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapToDiscount(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    @Override
    public Discount getById(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ONE)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapToDiscount(rs);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public boolean create(Discount discount) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(CREATE)) {
            ps.setInt(1, discount.getPoints_required());
            ps.setString(2, discount.getDescription());
            ps.setDouble(3, discount.getDiscount_value());
            ps.setString(4, discount.getDiscount_type().getValue());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(Discount discount) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setInt(1, discount.getPoints_required());
            ps.setString(2, discount.getDescription());
            ps.setDouble(3, discount.getDiscount_value());
            ps.setString(4, discount.getDiscount_type().getValue());
            ps.setInt(5, discount.getStatus().getCode());
            ps.setInt(6, discount.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public int getCountAllCustomer() {
        int result = 0;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SEARCH_COUNT_TOTAL)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt("count_total");
            }
        } catch (SQLException e) {
            System.err.println("Find all customers error: " + e.getMessage());
        }
        return result;
    }

    private Discount mapToDiscount(ResultSet resultSet) throws SQLException {
        return new Discount(
                resultSet.getInt("id"),
                resultSet.getInt("points_required"),
                resultSet.getString("description"),
                resultSet.getTimestamp("created_at"),
                resultSet.getDouble("discount_value"),
                DiscountType.fromString(resultSet.getString("discount_type")),
                DiscountStatus.fromCode(resultSet.getInt("status")));
    }

}
