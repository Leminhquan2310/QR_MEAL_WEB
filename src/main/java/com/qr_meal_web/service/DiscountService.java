package com.qr_meal_web.service;

import com.qr_meal_web.model.Discount;

import java.util.List;

public interface DiscountService {
    List<Discount> getAll(int limit, int page);

    Discount getById(int id);

    boolean create(Discount discount);

    boolean update(Discount discount);

    boolean delete(int id);

    int getTotalQuantityDiscount();
}
