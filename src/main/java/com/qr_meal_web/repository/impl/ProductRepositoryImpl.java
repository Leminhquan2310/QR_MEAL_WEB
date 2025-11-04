package com.qr_meal_web.repository.impl;

import com.qr_meal_web.enums.ProductStatus;
import com.qr_meal_web.model.Category;
import com.qr_meal_web.model.Menu;
import com.qr_meal_web.model.MenuProduct;
import com.qr_meal_web.model.Product;
import com.qr_meal_web.repository.ProductRepository;
import com.qr_meal_web.util.DBConnection;

import java.sql.*;
import java.util.*;

public class ProductRepositoryImpl implements ProductRepository {
    private final Connection connection = DBConnection.getConnection();
    private static final String SELECT_LIST = "SELECT p.*, c.id AS c_id, c.name AS c_name, c.icon AS c_icon FROM product p JOIN category c ON p.category_id = c.id";
    private static final String SELECT_ALL_PRODUCT = "SELECT p.*, c.id AS c_id, c.name AS c_name, c.icon AS c_icon FROM product p JOIN category c ON p.category_id = c.id LIMIT ? OFFSET ?";
    private static final String SELECT_ONE_BY_ID = "SELECT p.*, c.id AS c_id, c.name AS c_name, c.icon AS c_icon FROM product p JOIN category c ON p.category_id = c.id WHERE p.id = ?";
    private static final String SELECT_PRODUCT_FOR_CLIENT = "SELECT p.*, c.id AS c_id, c.name AS c_name, c.icon AS c_icon FROM product p JOIN category c ON p.category_id = c.id WHERE 1=1";
    private final String SELECT_MENU_PRODUCT = "select p.*, m.id as menu_id, m.name as menu_name, m.description as menu_desc, m.created_at as menu_created_at, c.id as c_id, c.name as c_name, c.icon as c_icon " +
            "from product p JOIN category c ON p.category_id = c.id JOIN menu_product mp ON p.id = mp.product_id JOIN menu m ON mp.menu_id = m.id";
    private static final String INSERT_PRODUCT = "INSERT INTO product (name, description, price, status, category_id, image, cooking_time) values (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_PRODUCT = "UPDATE product SET name =?, description =?, price = ?, status = ?, category_id = ?, image = ?, cooking_time = ?  WHERE id = ?";
    private static final String COUNT_PRODUCT_REFERENCE = "SELECT COUNT(*) AS result FROM orderdetail WHERE product_id = ?";
    private static final String DELETE_PRODUCT = "DELETE FROM product WHERE id = ?";
    private static final String FILTER_PRODUCT = "SELECT p.*, c.id AS c_id, c.name AS c_name, c.icon AS c_icon FROM product p JOIN category c ON p.category_id = c.id WHERE 1=1";

    @Override
    public List<Product> selectListProduct() {
        List<Product> products = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_LIST)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                products.add(mapToProducts(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return products;
    }

    @Override
    public List<Product> selectAllProduct(int limit, int offset) {
        List<Product> products = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_PRODUCT)) {
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                products.add(mapToProducts(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return products;
    }

    @Override
    public boolean insertProduct(Product p) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_PRODUCT)) {
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
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public Product selectById(int id) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ONE_BY_ID)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return mapToProducts(rs);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public boolean updateProduct(Product p) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_PRODUCT)) {
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
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean checkProductDeletable(int id) {
        try (PreparedStatement statement = connection.prepareStatement(COUNT_PRODUCT_REFERENCE)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt("result") == 0;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public List<Product> filterProduct(String filterString, List<Object> params) {
        List<Product> products = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(FILTER_PRODUCT + filterString)) {
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                products.add(mapToProducts(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return products;
    }

    @Override
    public List<Product> selectProductForClient(String filterString, List<Object> params) {
        List<Product> products = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_PRODUCT_FOR_CLIENT + filterString)) {
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                products.add(mapToProducts(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return products;
    }

    @Override
    public Map<Integer, List<MenuProduct>> selectMenuProductForClient() {
        Map<Integer, List<MenuProduct>> integerListMap = new HashMap<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_MENU_PRODUCT)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int menu_id = rs.getInt("menu_id");
                boolean existMenuId = integerListMap.containsKey(menu_id);
                if (existMenuId) {
                    List<MenuProduct> listTemp = integerListMap.get(menu_id);
                    listTemp.add(mapToMenuProduct(rs));
                    integerListMap.put(menu_id, listTemp);
                } else {
                    List<MenuProduct> menuProductList = new ArrayList<>();
                    menuProductList.add(mapToMenuProduct(rs));
                    integerListMap.put(menu_id, menuProductList);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return integerListMap;
    }

    @Override
    public boolean deleteProduct(int id) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_PRODUCT)) {
            statement.setInt(1, id);
            int rs = statement.executeUpdate();
            return rs > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public int countAll() {
        String sql = "SELECT COUNT(*) FROM product";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    @Override
    public int countFilter(String filterString, List<Object> params) {
        String sql = "SELECT COUNT(*) FROM product p WHERE 1=1" + filterString;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement st = conn.prepareStatement(sql);) {
            for (int i = 0; i < params.size(); i++) {
                st.setObject(i + 1, params.get(i));
            }
            ResultSet rs = st.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public Product mapToProducts(ResultSet rs) throws SQLException {
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
        return new Product(id, name, desc, price, ProductStatus.fromCode(statusCode), category, image, cooking_time);
    }



    private MenuProduct mapToMenuProduct(ResultSet rs) throws SQLException {
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
