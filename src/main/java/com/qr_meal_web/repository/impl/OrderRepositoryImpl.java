package com.qr_meal_web.repository.impl;

import com.qr_meal_web.enums.OrderStatus;
import com.qr_meal_web.model.CartItem;
import com.qr_meal_web.model.Order;
import com.qr_meal_web.repository.OrderRepository;
import com.qr_meal_web.service.CartService;
import com.qr_meal_web.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepositoryImpl implements OrderRepository {
    private final Connection connection = DBConnection.getConnection();
    private static final String INSERT_ORDER = "INSERT INTO `order` (table_id) values (?)";
    private static final String INSERT_ORDER_DETAIL = "INSERT INTO orderdetail (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_ORDER = "SELECT * FROM `order`";

    @Override
    public boolean insertOrder(int table_id, CartService cart) {
        try {
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
    public List<Order> selectAllOrder() {
        List<Order> orders = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_ORDER)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int table_id = rs.getInt("table_id");
                Timestamp created_at = rs.getTimestamp("created_at");
                OrderStatus status = OrderStatus.fromCode(rs.getInt("status"));
                orders.add(new Order(id, table_id, created_at, status));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return orders;
    }


}
