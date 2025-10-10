package com.qr_meal_web.service.impl;

import com.qr_meal_web.model.Order;
import com.qr_meal_web.model.OrderDetail;
import com.qr_meal_web.repository.OrderRepository;
import com.qr_meal_web.repository.impl.OrderRepositoryImpl;
import com.qr_meal_web.service.CartService;
import com.qr_meal_web.service.OrderService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository = new OrderRepositoryImpl();

    @Override
    public boolean insertOrder(int table_id, CartService cart) {
        return orderRepository.insertOrder(table_id, cart);
    }

    @Override
    public List<Order> selectAllOrder(int limit, int page) {
        int offset = (page - 1) * limit;
        return orderRepository.selectAllOrder(limit, offset);
    }

    @Override
    public Order selectOrderById(int id) {
        return orderRepository.selectOrderById(id);
    }

    @Override
    public List<OrderDetail> selectOrderDetailByOrderId(int id) {
        return orderRepository.selectOrderDetailByOrderId(id);
    }

    @Override
    public boolean updateOrder(int id, int status) {
        return orderRepository.updateOrder(id, status);
    }

    @Override
    public boolean deleteOrder(int id) {
        boolean isReferenced = orderRepository.isReferenced(id);
        if (isReferenced) return false;
        return orderRepository.deleteOrder(id);
    }

    @Override
    public List<Order> filterOrder(int idOrTableId, int status, String createdFrom, String createdTo, int limit, int page) {
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();

        if (idOrTableId > 0) {
            sql.append(" AND (id = ? OR table_id = ?)");
            params.add(idOrTableId);
            params.add(idOrTableId);
        }

        if (status >= 0) {
            sql.append(" AND status = ?");
            params.add(status);
        }

        // Filter theo khoảng ngày
        if (createdFrom != null && !createdFrom.isEmpty() && createdTo != null && !createdTo.isEmpty()) {
            sql.append(" AND created_at BETWEEN ? AND ?");
            params.add(Timestamp.valueOf(createdFrom + " 00:00:00"));
            params.add(Timestamp.valueOf(createdTo + " 23:59:59"));
        } else if (createdFrom != null && !createdFrom.isEmpty()) {
            sql.append(" AND created_at >= ?");
            params.add(Timestamp.valueOf(createdFrom + " 00:00:00"));
        } else if (createdTo != null && !createdTo.isEmpty()) {
            sql.append(" AND created_at <= ?");
            params.add(Timestamp.valueOf(createdTo + " 23:59:59"));
        }

        int offset = (page - 1) * limit;
        sql.append(" ORDER BY created_at DESC LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);
        return orderRepository.filterOrder(sql.toString(), params);
    }

    @Override
    public int getTotalOrders() {
        return orderRepository.countAll();
    }

    @Override
    public int getTotalOrdersFilter(int idOrTableId, int status, String createdFrom, String createdTo) {
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();

        if (idOrTableId > 0) {
            sql.append(" AND (id = ? OR table_id = ?)");
            params.add(idOrTableId);
            params.add(idOrTableId);
        }

        if (status >= 0) {
            sql.append(" AND status = ?");
            params.add(status);
        }

        // Filter theo khoảng ngày
        if (createdFrom != null && !createdFrom.isEmpty() && createdTo != null && !createdTo.isEmpty()) {
            sql.append(" AND created_at BETWEEN ? AND ?");
            params.add(Timestamp.valueOf(createdFrom + " 00:00:00"));
            params.add(Timestamp.valueOf(createdTo + " 23:59:59"));
        } else if (createdFrom != null && !createdFrom.isEmpty()) {
            sql.append(" AND created_at >= ?");
            params.add(Timestamp.valueOf(createdFrom + " 00:00:00"));
        } else if (createdTo != null && !createdTo.isEmpty()) {
            sql.append(" AND created_at <= ?");
            params.add(Timestamp.valueOf(createdTo + " 23:59:59"));
        }
        return orderRepository.countFilter(sql.toString(), params);
    }


}
