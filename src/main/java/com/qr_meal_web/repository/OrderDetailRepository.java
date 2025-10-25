package com.qr_meal_web.repository;

import com.qr_meal_web.model.CartItem;
import com.qr_meal_web.model.OrderDetail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface OrderDetailRepository {
    void insertOrderDetail(Connection connection, int order_id, CartItem cartItem) throws SQLException;

    List<OrderDetail> selectOrderDetailByOrderId(int id);

    List<OrderDetail> selectOrderDetailByTableId(int id);
}
