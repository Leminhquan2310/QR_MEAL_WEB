package com.qr_meal_web.repository.impl;

import com.qr_meal_web.enums.OrderStatus;
import com.qr_meal_web.model.*;
import com.qr_meal_web.repository.OrderRepository;
import com.qr_meal_web.service.CartService;
import com.qr_meal_web.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepositoryImpl implements OrderRepository {
    private Connection connection;
    private static final String SELECT_ALL_ORDER = "SELECT * FROM `order` ORDER BY created_at DESC LIMIT ? OFFSET ?";
    private static final String SELECT_ORDER_BY_ID = "SELECT * FROM `order` WHERE id = ?";
    private static final String SELECT_ORDER_BY_TABLE_ID = "SELECT * FROM `order` WHERE table_id = ? AND status NOT IN (3, 4)";
    private static final String FILTER_ORDER = "SELECT * FROM `order` WHERE 1=1";
    private static final String INSERT_ORDER = "INSERT INTO `order` (table_id) values (?)";
    private static final String UPDATE_ORDER_STATUS = "UPDATE `order` SET status = ? WHERE id = ?";
    private static final String COUNT_ORDER_IN_INVOICE = "SELECT count(*) FROM invoice WHERE order_id = ?";
    private static final String COUNT_ORDER_IN_PAYMENT = "SELECT count(*) FROM payment WHERE order_id = ?";
    private static final String DELETE_ORDER = "DELETE FROM `order` WHERE id = ?";
    private static final String DELETE_ORDER_DETAIL = "DELETE FROM orderdetail WHERE order_id = ?";

    @Override
    public int insertOrder(Connection connection, int table_id, CartService cart) throws SQLException {
        int result = -1;
        PreparedStatement statementOrder = connection.prepareStatement(INSERT_ORDER, Statement.RETURN_GENERATED_KEYS);
        statementOrder.setInt(1, table_id);
        statementOrder.executeUpdate();
        ResultSet rs = statementOrder.getGeneratedKeys();
        if (rs.next()) result = rs.getInt(1);
        return result;
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
    public Order selectOrderByTableIdAvailable(int id) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ORDER_BY_TABLE_ID)) {
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
    public boolean changOrderStatus(Connection connection, int order_id, OrderStatus status) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_ORDER_STATUS)) {
            statement.setInt(1, status.getCode());
            statement.setInt(2, order_id);
            statement.executeUpdate();
            return true;
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
