package com.qr_meal_web.repository;

import com.qr_meal_web.model.Discount;

import java.util.List;

public interface DiscountRepository {
    List<Discount> getAll(int limit, int offset);

    Discount getById(int id);

    boolean create(Discount discount);

    boolean update(Discount discount);

    boolean delete(int id);

    int getCountAllCustomer();

    List<Discount> getDiscountsLessThanPhone(String phone);
}
