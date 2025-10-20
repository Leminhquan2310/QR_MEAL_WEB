package com.qr_meal_web.service;

import com.qr_meal_web.model.OrderStatusLog;

import java.sql.SQLException;
import java.util.List;

public interface OrderStatusLogService {
    boolean save(OrderStatusLog log) throws SQLException;

    List<OrderStatusLog> findByOrderId(int orderId) throws SQLException;
}
