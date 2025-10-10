package com.qr_meal_web.repository.impl;

import com.qr_meal_web.enums.CategoryStatus;
import com.qr_meal_web.enums.OrderStatus;
import com.qr_meal_web.enums.ProductStatus;
import com.qr_meal_web.model.*;
import com.qr_meal_web.repository.OrderRepository;
import com.qr_meal_web.service.CartService;
import com.qr_meal_web.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepositoryImpl implements OrderRepository {
    private Connection connection;
    private static final String INSERT_ORDER = "INSERT INTO `order` (table_id) values (?)";
    private static final String INSERT_ORDER_DETAIL = "INSERT INTO orderdetail (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_ORDER = "SELECT * FROM `order` ORDER BY created_at DESC LIMIT ? OFFSET ?";
    private static final String SELECT_ORDER_BY_ID = "SELECT * FROM `order` WHERE id = ?";
    private static final String UPDATE_ORDER = "UPDATE `order` SET status = ? WHERE id = ?";
    private static final String SELECT_ORDER_DETAIL_BY_ORDER_ID =
            "SELECT od.order_id, od.quantity, od.price as unit_price, p.*, c.id AS c_id, c.name AS c_name, c.icon AS c_icon " +
                    "FROM orderdetail od " +
                    "JOIN product p ON od.product_id = p.id " +
                    "JOIN category c ON p.category_id = c.id WHERE od.order_id = ?";
    private static final String COUNT_ORDER_IN_INVOICE = "SELECT count(*) FROM invoice WHERE order_id = ?";
    private static final String COUNT_ORDER_IN_PAYMENT = "SELECT count(*) FROM payment WHERE order_id = ?";
    private static final String DELETE_ORDER = "DELETE FROM `order` WHERE id = ?";
    private static final String DELETE_ORDER_DETAIL = "DELETE FROM orderdetail WHERE order_id = ?";
    private static final String FILTER_ORDER = "SELECT * FROM `order` WHERE 1=1";

    @Override
    public boolean insertOrder(int table_id, CartService cart) {
        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement statementOrder = connection.prepareStatement(INSERT_ORDER, Statement.RETURN_GENERATED_KEYS);

            statementOrder.setInt(1, table_id);
            int rowAffected = statementOrder.executeUpdate();
            ResultSet rs = statementOrder.getGeneratedKeys();

            int order_id = -1;
            if (rs.next()) order_id = rs.getInt(1);

            if (rowAffected != 1) {
                connection.rollback();
                return false;
            }

            for (CartItem item : cart.getItems()) {
                PreparedStatement statementOrderDetail = connection.prepareStatement(INSERT_ORDER_DETAIL);
                statementOrderDetail.setInt(1, order_id);
                statementOrderDetail.setInt(2, item.getProduct().getId());
                statementOrderDetail.setInt(3, item.getQuantity());
                statementOrderDetail.setDouble(4, item.getProduct().getPrice());
                statementOrderDetail.executeUpdate();
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            System.out.println(e.getMessage());
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public List<Order> selectAllOrder(int limit, int offset) {
        List<Order> orders = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_ORDER)) {
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int table_id = rs.getInt("table_id");
                Timestamp created_at = rs.getTimestamp("created_at");
                OrderStatus status = OrderStatus.fromCode(rs.getInt("status"));
                orders.add(new Order(id, table_id, created_at, status));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return orders;
    }

    @Override
    public Order selectOrderById(int id) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ORDER_BY_ID)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int order_id = rs.getInt("id");
                int table_id = rs.getInt("table_id");
                Timestamp created_at = rs.getTimestamp("created_at");
                OrderStatus status = OrderStatus.fromCode(rs.getInt("status"));
                return new Order(order_id, table_id, created_at, status);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<OrderDetail> selectOrderDetailByOrderId(int id) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ORDER_DETAIL_BY_ORDER_ID)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int order_id = rs.getInt("order_id");
                int quantity = rs.getInt("quantity");
                double unit_price = rs.getDouble("unit_price");

                int pro_id = rs.getInt("id");
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
                Product product = new Product(pro_id, name, desc, price, ProductStatus.fromCode(statusCode), cate, image, cooking_time);
                orderDetails.add(new OrderDetail(order_id, pro_id, quantity, unit_price, product));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return orderDetails;
    }

    @Override
    public boolean updateOrder(int id, int status) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_ORDER)) {
            statement.setInt(1, status);
            statement.setInt(2, id);
            int result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean deleteOrder(int id) {
        try (Connection connection = DBConnection.getConnection()) {
            PreparedStatement statementDetail = connection.prepareStatement(DELETE_ORDER_DETAIL);
            statementDetail.setInt(1, id);
            PreparedStatement statementOrder = connection.prepareStatement(DELETE_ORDER);
            statementOrder.setInt(1, id);

            int resultDetail = statementDetail.executeUpdate();
            int resultOrder = statementOrder.executeUpdate();

            return resultDetail > 0 && resultOrder > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean isReferenced(int id) {
        try (Connection connection = DBConnection.getConnection()) {
            PreparedStatement statementInvoice = connection.prepareStatement(COUNT_ORDER_IN_INVOICE);
            statementInvoice.setInt(1, id);
            PreparedStatement statementPayment = connection.prepareStatement(COUNT_ORDER_IN_PAYMENT);
            statementPayment.setInt(1, id);

            int resultInvoice = statementInvoice.executeUpdate();
            int resultPayment = statementPayment.executeUpdate();

            return resultPayment > 0 || resultInvoice > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public List<Order> filterOrder(String filterString, List<Object> params) {
        List<Order> orders = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FILTER_ORDER + filterString)) {
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int order_id = rs.getInt("id");
                int table_id = rs.getInt("table_id");
                Timestamp created_at = rs.getTimestamp("created_at");
                OrderStatus status = OrderStatus.fromCode(rs.getInt("status"));
                orders.add(new Order(order_id, table_id, created_at, status));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return orders;
    }

    @Override
    public int countAll() {
        String sql = "SELECT COUNT(*) FROM `order`";
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
        String sql = "SELECT COUNT(*) FROM `order` WHERE 1=1" + filterString;
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

}
