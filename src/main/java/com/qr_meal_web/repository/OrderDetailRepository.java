package com.qr_meal_web.repository;

import com.qr_meal_web.model.OrderDetail;

import java.util.List;

public interface OrderDetailRepository {
    List<OrderDetail> selectOrderDetailByOrderId(int id);

    List<OrderDetail> selectOrderDetailByTableId(int id);
}
