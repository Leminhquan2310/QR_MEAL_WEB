package com.qr_meal_web.repository.impl;

import com.qr_meal_web.enums.OrderStatus;
import com.qr_meal_web.model.Employee;
import com.qr_meal_web.model.OrderStatusLog;
import com.qr_meal_web.repository.OrderStatusLogRepository;
import com.qr_meal_web.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderStatusLogRepositoryImpl implements OrderStatusLogRepository {
    private static final String INSERT_ORDER_STATUS_LOG = "INSERT INTO order_status_log (order_id, old_status, new_status, changed_by, note) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_ORDER_STATUS_BY_ORDER_ID = "SELECT osl.*, e.name AS e_name FROM order_status_log osl JOIN employee e ON osl.changed_by = e.id WHERE order_id = ? ORDER BY changed_at DESC";


    @Override
    public boolean save(Connection connection, OrderStatusLog log) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_ORDER_STATUS_LOG)) {
            ps.setInt(1, log.getOrderId());
            ps.setInt(2, log.getOld_status().getCode());
            ps.setInt(3, log.getNew_status().getCode());
            ps.setInt(4, log.getChanged_by().getId());
            ps.setString(5, log.getNote());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public List<OrderStatusLog> findByOrderId(int orderId) {
        List<OrderStatusLog> orderStatusLogs = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_ORDER_STATUS_BY_ORDER_ID)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderStatusLog l = new OrderStatusLog();
                    l.setId(rs.getInt("id"));
                    l.setOrderId(rs.getInt("order_id"));
                    l.setOld_status(OrderStatus.fromCode(rs.getInt("old_status")));
                    l.setNew_status(OrderStatus.fromCode(rs.getInt("new_status")));
                    Employee employee = new Employee();
                    employee.setId(rs.getInt("changed_by"));
                    employee.setName(rs.getString("e_name"));
                    l.setChanged_by(employee);
                    l.setChanged_at(rs.getTimestamp("changed_at"));
                    l.setNote(rs.getString("note"));
                    orderStatusLogs.add(l);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return orderStatusLogs;
    }
}
