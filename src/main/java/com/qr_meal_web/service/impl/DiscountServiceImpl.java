package com.qr_meal_web.service.impl;

import com.qr_meal_web.model.Discount;
import com.qr_meal_web.repository.DiscountRepository;
import com.qr_meal_web.repository.impl.DiscountRepositoryImpl;
import com.qr_meal_web.service.DiscountService;

import java.util.List;

public class DiscountServiceImpl implements DiscountService {
    private final DiscountRepository repo = new DiscountRepositoryImpl();

    @Override
    public List<Discount> getAll(int limit, int page) {
        int offset = (page - 1) * limit;
        return repo.getAll(limit, offset);
    }

    @Override
    public Discount getById(int id) {
        return repo.getById(id);
    }

    @Override
    public boolean create(Discount discount) {
        return repo.create(discount);
    }

    @Override
    public boolean update(Discount discount) {
        return repo.update(discount);
    }

    @Override
    public boolean delete(int id) {
        return repo.delete(id);
    }

    @Override
    public int getTotalQuantityDiscount() {
        return repo.getCountAllCustomer();
    }
}
