package com.qr_meal_web.repository.impl;

import com.qr_meal_web.model.Customer;
import com.qr_meal_web.repository.CustomerRepository;
import com.qr_meal_web.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepositoryImpl implements CustomerRepository {
    private static final String INSERT_SQL = "INSERT INTO customer (name, phone, points) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE customer SET name = ?, phone = ?, points = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM customer WHERE id = ?";
    private static final String SELECT_BY_ID = "SELECT * FROM customer WHERE id = ?";
    private static final String SELECT_BY_PHONE = "SELECT * FROM customer WHERE phone = ?";
    private static final String SELECT_ALL = "SELECT * FROM customer ORDER BY created_at DESC LIMIT ? OFFSET ?";
    private static final String SEARCH_BY_NAME = "SELECT * FROM customer WHERE name LIKE ?";
    private static final String SEARCH_COUNT_TOTAL= "SELECT count(*) AS count_total FROM customer";

    @Override
    public boolean insert(Customer customer) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhone());
            ps.setInt(3, customer.getPoints());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Insert customer error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Customer customer) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhone());
            ps.setInt(3, customer.getPoints());
            ps.setInt(4, customer.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Update customer error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Delete customer error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Customer findById(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapResultSet(rs);
        } catch (SQLException e) {
            System.err.println("Find customer by ID error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Customer findByPhone(String phone) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_PHONE)) {
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapResultSet(rs);
        } catch (SQLException e) {
            System.err.println("Find customer by phone error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Customer> findAll(int limit, int offset) {
        List<Customer> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Find all customers error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<Customer> searchByName(String name) {
        List<Customer> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SEARCH_BY_NAME)) {
            ps.setString(1, "%" + name + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Search customers by name error: " + e.getMessage());
        }
        return list;
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

    // Helper method to map ResultSet -> Customer
    private Customer mapResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String phone = rs.getString("phone");
        int points = rs.getInt("points");
        Timestamp createdAt = rs.getTimestamp("created_at");
        return new Customer(id, name, phone, points, createdAt);
    }
}
