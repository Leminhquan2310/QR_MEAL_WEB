package com.qr_meal_web.service.impl;

import com.qr_meal_web.model.OrderDetail;
import com.qr_meal_web.repository.OrderDetailRepository;
import com.qr_meal_web.repository.impl.OrderDetailRepositoryImpl;
import com.qr_meal_web.service.OrderDetailService;

import java.util.List;

public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailRepository orderDetailRepository = new OrderDetailRepositoryImpl();

    @Override
    public List<OrderDetail> selectOrderDetailByOrderId(int id) {
        return orderDetailRepository.selectOrderDetailByOrderId(id);
    }

    @Override
    public List<OrderDetail> selectOrderDetailByTableId(int id) {
        return orderDetailRepository.selectOrderDetailByTableId(id);
    }
}
