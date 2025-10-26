package com.qr_meal_web.service;

import com.qr_meal_web.model.OrderStatusLog;

import java.util.List;

public interface OrderStatusLogService {
    List<OrderStatusLog> findByOrderId(int orderId);
}
