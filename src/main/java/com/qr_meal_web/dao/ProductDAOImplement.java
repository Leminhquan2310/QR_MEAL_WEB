package com.qr_meal_web.dao;

import com.qr_meal_web.enums.ProductStatus;
import com.qr_meal_web.model.Category;
import com.qr_meal_web.model.Product;
import com.qr_meal_web.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImplement implements IProductDAO {
    private static final String SELECT_ALL_PRODUCT = "SELECT p.*, c.id AS c_id, c.name AS c_name, c.icon AS c_icon FROM product p JOIN category c ON p.category_id = c.id";
    private static final String INSERT_PRODUCT = "INSERT INTO product (name, description, price, status, category_id, image, cooking_time) values (?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ONE_BY_ID = "SELECT p.*, c.id AS c_id, c.name AS c_name, c.icon AS c_icon FROM product p JOIN category c ON p.category_id = c.id WHERE p.id = ?";
    private static final String UPDATE_PRODUCT = "UPDATE product SET name =?, description =?, price = ?, status = ?, category_id = ?, image = ?, cooking_time = ?  WHERE id = ?";
    private static final String COUNT_PRODUCT_REFERENCE = "SELECT COUNT(*) AS result FROM orderdetail WHERE product_id = ?";
    private static final String DELETE_PRODUCT = "DELETE FROM product WHERE id = ?";

    @Override
    public List<Product> selectAllProduct() {
        List<Product> products = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(SELECT_ALL_PRODUCT)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String desc = rs.getString("description");
                double price = rs.getDouble("price");
                int statusCode = rs.getInt("status");
                Category category = new Category();
                category.setId(rs.getInt("c_id"));
                category.setName(rs.getString("c_name"));
                category.setIcon(rs.getString("c_icon"));
                String image = rs.getString("image");
                int cooking_time = rs.getInt("cooking_time");
                products.add(new Product(id, name, desc, price, ProductStatus.fromCode(statusCode), category, image, cooking_time));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return products;
    }

    @Override
    public boolean insertProduct(Product p) {
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(INSERT_PRODUCT)) {
            statement.setString(1, p.getName());
            statement.setString(2, p.getDescription());
            statement.setDouble(3, p.getPrice());
            statement.setInt(4, p.getStatus().getCode());
            statement.setInt(5, p.getCategory().getId());
            statement.setString(6, p.getImage());
            statement.setInt(7, p.getCooking_time());

            int result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Product selectById(int id) {
        Product product = null;
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(SELECT_ONE_BY_ID)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String desc = rs.getString("description");
                double price = rs.getDouble("price");
                int statusCode = rs.getInt("status");
                Category category = new Category();
                category.setId(rs.getInt("c_id"));
                category.setName(rs.getString("c_name"));
                category.setIcon(rs.getString("c_icon"));
                String image = rs.getString("image");
                int cooking_time = rs.getInt("cooking_time");
                product = new Product(id, name, desc, price, ProductStatus.fromCode(statusCode), category, image, cooking_time);
                return product;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateProduct(Product p) {
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_PRODUCT)) {
            statement.setString(1, p.getName());
            statement.setString(2, p.getDescription());
            statement.setDouble(3, p.getPrice());
            statement.setInt(4, p.getStatus().getCode());
            statement.setInt(5, p.getCategory().getId());
            statement.setString(6, p.getImage());
            statement.setInt(7, p.getCooking_time());
            statement.setInt(8, p.getId());

            int result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean checkProductDeletable(int id) {
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(COUNT_PRODUCT_REFERENCE)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt("result") == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Product> filterProduct(String keyword, double minPrice, double maxPrice, int category, int status) {
        List<Product> products = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT p.*, c.id AS c_id, c.name AS c_name, c.icon AS c_icon FROM product p JOIN category c ON p.category_id = c.id WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (!keyword.isEmpty()) {
            sql.append(" AND (p.name LIKE ? OR p.description LIKE ?)");
            params.add("%" + keyword + "%");
            params.add("%" + keyword + "%");
        }

        if (minPrice > 0) {
            sql.append(" AND price >= ?");
            params.add(minPrice);
        }

        if (maxPrice > 0) {
            sql.append(" AND price <= ?");
            params.add(maxPrice);
        }

        if (category > 0) {
            sql.append(" AND category_id = ?");
            params.add(category);
        }

        if (status >= 0) {
            sql.append(" AND p.status = ?");
            params.add(status);
        }

        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String desc = rs.getString("description");
                double price = rs.getDouble("price");
                int statusCode = rs.getInt("status");
                Category cate = new Category();
                cate.setId(rs.getInt("c_id"));
                cate.setName(rs.getString("c_name"));
                cate.setIcon(rs.getString("c_icon"));
                String image = rs.getString("image");
                int cooking_time = rs.getInt("cooking_time");
                products.add(new Product(id, name, desc, price, ProductStatus.fromCode(statusCode), cate, image, cooking_time));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public boolean deleteProduct(int id) {
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_PRODUCT)) {
            statement.setInt(1, id);
            int rs = statement.executeUpdate();
            return rs > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
