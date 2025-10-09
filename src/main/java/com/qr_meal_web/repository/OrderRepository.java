package com.qr_meal_web.repository;

import com.qr_meal_web.model.Order;
import com.qr_meal_web.service.CartService;

import java.util.List;

public interface OrderRepository {
    boolean insertOrder(int table_id, CartService cart);

    List<Order> selectAllOrder();
}
