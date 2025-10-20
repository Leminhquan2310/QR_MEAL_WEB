package com.qr_meal_web.repository;

import com.qr_meal_web.model.Order;
import com.qr_meal_web.model.OrderDetail;
import com.qr_meal_web.service.CartService;

import java.sql.SQLException;
import java.util.List;

public interface OrderRepository {
    boolean insertOrder(int table_id, CartService cart);

    List<Order> selectAllOrder(int limit, int offset);

    Order selectOrderById(int id);

    boolean deleteOrder(int id);

    boolean changOrderStatus(Order order) throws SQLException;

    boolean isReferenced(int id);

    List<Order> filterOrder(String filterString, List<Object> params);

    int countAll();

    int countFilter(String filterString, List<Object> params);
}
