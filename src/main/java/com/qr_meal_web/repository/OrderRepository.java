package com.qr_meal_web.repository;

import com.qr_meal_web.enums.OrderStatus;
import com.qr_meal_web.model.Order;
import com.qr_meal_web.service.CartService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface OrderRepository {
    int insertOrder(Connection connection, int table_id, CartService cart) throws SQLException;

    List<Order> selectAllOrder(int limit, int offset);

    Order selectOrderById(int id);

    Order selectOrderByTableIdAvailable(int id);

    boolean deleteOrder(Connection connection, int id);

    boolean changOrderStatus(Connection connection, int order_id, OrderStatus status) throws SQLException;

    boolean isReferenced(int id);

    List<Order> filterOrder(String filterString, List<Object> params);

    int countAll();

    int countFilter(String filterString, List<Object> params);
}
