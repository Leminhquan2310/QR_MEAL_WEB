package com.qr_meal_web.service;

import com.qr_meal_web.model.Employee;
import com.qr_meal_web.model.Order;
import com.qr_meal_web.model.OrderDetail;
import com.qr_meal_web.model.OrderStatusLog;

import java.sql.SQLException;
import java.util.List;

public interface OrderService {
    boolean insertOrder(int table_id, CartService cart);

    List<Order> selectAllOrder(int limit, int page);

    Order selectOrderById(int id);

    List<OrderDetail> selectOrderDetailByOrderId(int id);

    boolean deleteOrder(int id);

    List<Order> filterOrder(int idOrTableId, int status, String createdFrom, String createdTo, int limit, int page);

    int getTotalOrders();

    int getTotalOrdersFilter(int idOrTableId, int status, String createdFrom, String createdTo);

    boolean changeOrderStatus(int orderId, int newStatus, Employee changedBy, String note);

    List<OrderStatusLog> getOrderStatusLogs(int orderId);
}
