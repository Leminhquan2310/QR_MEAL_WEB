package com.qr_meal_web.service;

import com.qr_meal_web.model.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetail> selectOrderDetailByOrderId(int id);

    List<OrderDetail> selectOrderDetailByTableId(int id);
}
