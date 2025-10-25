package com.qr_meal_web.service;

import com.qr_meal_web.model.OrderStatusLog;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface OrderStatusLogService {
    List<OrderStatusLog> findByOrderId(int orderId);
}
