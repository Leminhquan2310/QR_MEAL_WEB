package com.qr_meal_web.service.impl;

import com.qr_meal_web.model.CartItem;
import com.qr_meal_web.model.Product;
import com.qr_meal_web.service.CartService;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CartServiceImpl implements CartService {
    private Map<Integer, CartItem> items = new LinkedHashMap<>();
    private int totalQuantity = 0;
    private double totalAmount = 0.0;

    public CartServiceImpl() {
    }

    @Override
    public void addItem(Product p, int qty) {
        CartItem item = items.get(p.getId());
        if (item == null) items.put(p.getId(), new CartItem(p, qty));
        else item.setQuantity(item.getQuantity() + qty);
        totalQuantity += qty;
        totalAmount += p.getPrice() * qty;
    }

    @Override
    public void removeItem(int id) {
        CartItem item = items.get(id);
        totalQuantity -= item.getQuantity();
        totalAmount -= item.getQuantity() * item.getProduct().getPrice();
        items.remove(id);
    }

    @Override
    public void updateQuantity(int id, int delta) {
        CartItem item = items.get(id);
        if (item.getQuantity() == 1 && delta < 0) {
            return;
        }
        item.setQuantity(item.getQuantity() + delta);
        items.put(id, item);
        totalAmount += item.getProduct().getPrice() * delta;
        totalQuantity += delta;
    }

    @Override
    public void clear() {
        items.clear();
    }

    @Override
    public Collection<CartItem> getItems() {
        return items.values();
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public int getTotalQuantity() {
        return totalQuantity;
    }

    @Override
    public double getTotalAmount() {
        return totalAmount;
    }
}
