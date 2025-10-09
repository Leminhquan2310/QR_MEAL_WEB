package com.qr_meal_web.service;

import com.qr_meal_web.model.Order;

import java.util.List;

public interface OrderService {
    boolean insertOrder(int table_id, CartService cart);

    List<Order> selectAllOrder();
}
