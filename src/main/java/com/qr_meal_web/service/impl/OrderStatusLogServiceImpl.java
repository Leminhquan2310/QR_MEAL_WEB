package com.qr_meal_web.service.impl;

import com.qr_meal_web.model.OrderStatusLog;
import com.qr_meal_web.repository.OrderStatusLogRepository;
import com.qr_meal_web.repository.impl.OrderStatusLogRepositoryImpl;
import com.qr_meal_web.service.OrderStatusLogService;

import java.sql.SQLException;
import java.util.List;

public class OrderStatusLogServiceImpl implements OrderStatusLogService {
    private final OrderStatusLogRepository orderStatusLogRepository = new OrderStatusLogRepositoryImpl();

    @Override
    public boolean save(OrderStatusLog log) throws SQLException {
        return orderStatusLogRepository.save(log);
    }

    @Override
    public List<OrderStatusLog> findByOrderId(int orderId) throws SQLException {
        return orderStatusLogRepository.findByOrderId(orderId);
    }
}
