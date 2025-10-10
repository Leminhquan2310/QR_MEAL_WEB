package com.qr_meal_web.service;

import com.qr_meal_web.model.Order;
import com.qr_meal_web.model.OrderDetail;

import java.util.List;

public interface OrderService {
    boolean insertOrder(int table_id, CartService cart);

    List<Order> selectAllOrder(int limit, int page);

    Order selectOrderById(int id);

    List<OrderDetail> selectOrderDetailByOrderId(int id);

    boolean updateOrder(int id, int status);

    boolean deleteOrder(int id);

    List<Order> filterOrder(int idOrTableId, int status, String createdFrom, String createdTo, int limit, int page);

    int getTotalOrders();

    int getTotalOrdersFilter(int idOrTableId, int status, String createdFrom, String createdTo);
}
