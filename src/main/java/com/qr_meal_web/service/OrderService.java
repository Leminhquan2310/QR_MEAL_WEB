package com.qr_meal_web.service;

import com.qr_meal_web.model.*;

import java.sql.SQLException;
import java.util.List;

public interface OrderService {
    boolean insertOrder(int table_id, CartService cart);

    List<Order> selectAllOrder(int limit, int page);

    Order selectOrderById(int id);

    Order selectOrderByTableIdAvailable(int id);

    boolean deleteOrder(int id);

    List<Order> filterOrder(int idOrTableId, int status, String createdFrom, String createdTo, int limit, int page);

    int getTotalOrders();

    int getTotalOrdersFilter(int idOrTableId, int status, String createdFrom, String createdTo);

    boolean changeOrderStatus(int orderId, int newStatus, Employee changedBy, String note);

    boolean completeOrder(int orderId, String phone, String pointOption, Discount discount_id, String paymentMethod, Employee employee);
}