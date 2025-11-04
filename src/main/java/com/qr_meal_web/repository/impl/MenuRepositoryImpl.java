package com.qr_meal_web.repository.impl;

import com.qr_meal_web.enums.MenuStatus;
import com.qr_meal_web.model.Menu;
import com.qr_meal_web.repository.MenuRepository;
import com.qr_meal_web.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuRepositoryImpl implements MenuRepository {
    private final String SELECT_ALL = "SELECT * FROM menu";
    private final String SELECT_ONE = "SELECT * FROM menu WHERE id = ?";
    private final String CREATE = "INSERT INTO menu (name, description) VALUES (?, ?)";
    private final String UPDATE = "UPDATE menu SET name = ?, description = ? WHERE id = ?";
    private final String DELETE = "DELETE FROM menu WHERE id = ?";

    @Override
    public List<Menu> getAllMenus() {
        List<Menu> menus = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                menus.add(mapToMenu(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return menus;
    }

    @Override
    public Menu getMenuById(int id) {
        Menu menu = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ONE)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                menu = mapToMenu(rs);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return menu;
    }

    @Override
    public int addMenu(Connection connection, Menu menu) {
        try (PreparedStatement ps = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, menu.getName());
            ps.setString(2, menu.getDescription());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    @Override
    public boolean updateMenu(Connection connection, Menu menu) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setString(1, menu.getName());
            ps.setString(2, menu.getDescription());
            ps.setInt(3, menu.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteMenu(Connection connection, int id) {
        try (PreparedStatement ps = connection.prepareStatement(DELETE)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private Menu mapToMenu(ResultSet rs) throws SQLException {
        return new Menu(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                MenuStatus.fromCode(rs.getInt("status")),
                rs.getTimestamp("created_at")
        );
    }
}
