package com.qr_meal_web.repository.impl;

import com.qr_meal_web.model.Menu;
import com.qr_meal_web.model.MenuProduct;
import com.qr_meal_web.model.Product;
import com.qr_meal_web.repository.MenuProductRepository;
import com.qr_meal_web.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuProductRepositoryImpl implements MenuProductRepository {
    private final String SELECT_BY_MENU_ID = "select p.*, m.id as menu_id, m.name as menu_name, m.description as menu_desc, m.created_at as menu_created_at, c.id as c_id, c.name as c_name, c.icon as c_icon " +
            "from product p JOIN category c ON p.category_id = c.id JOIN menu_product mp ON p.id = mp.product_id JOIN menu m ON mp.menu_id = m.id WHERE mp.menu_id = ?";
    private final String CREATE = "INSERT INTO menu_product (menu_id, product_id) VALUES (?, ?)";
    private final String DELETE = "DELETE FROM menu_product WHERE menu_id = ?";

    @Override
    public List<MenuProduct> findById(int menuId) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_MENU_ID)) {
            ps.setInt(1, menuId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                menuProducts.add(mapToMenuProduct(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return menuProducts;
    }

    @Override
    public void addMenuProduct(Connection connection, int menu_id, int product_id) {
        try (PreparedStatement ps = connection.prepareStatement(CREATE)) {
            ps.setInt(1, menu_id);
            ps.setInt(2, product_id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteMenuProduct(Connection connection, int menu_id) {
        try (PreparedStatement ps = connection.prepareStatement(DELETE)) {
            ps.setInt(1, menu_id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public MenuProduct mapToMenuProduct(ResultSet rs) throws SQLException {
        int menu_id = rs.getInt("menu_id");
        int product_id = rs.getInt("id");
        String menu_name = rs.getString("menu_name");
        String menu_desc = rs.getString("menu_desc");
        Menu menu = new Menu();
        menu.setId(menu_id);
        menu.setName(menu_name);
        menu.setDescription(menu_desc);
        Product product = new ProductRepositoryImpl().mapToProducts(rs);
        Timestamp created_at = rs.getTimestamp("menu_created_at");
        return new MenuProduct(menu_id, product_id, menu, product, created_at);
    }
}
