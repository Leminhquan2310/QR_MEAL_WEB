package com.qr_meal_web.service.impl;

import com.qr_meal_web.model.Order;
import com.qr_meal_web.service.CartService;
import com.qr_meal_web.service.OrderService;

import java.util.List;

public class OrderServiceImpl implements OrderService {
    private final OrderService orderService = new OrderServiceImpl();

    @Override
    public boolean insertOrder(int table_id, CartService cart) {
        return orderService.insertOrder(table_id, cart);
    }

    @Override
    public List<Order> selectAllOrder() {
        return orderService.selectAllOrder();
    }
}
