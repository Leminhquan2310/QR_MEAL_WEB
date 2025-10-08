package com.qr_meal_web.dao;

import com.qr_meal_web.model.CartItem;
import com.qr_meal_web.model.Product;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Cart {
    private Map<Integer, CartItem> items = new LinkedHashMap<>();
    private int totalQuantity = 0;
    private double totalAmount = 0.0;

    public void addItem(Product p, int qty) {
        CartItem item = items.get(p.getId());
        if (item == null) items.put(p.getId(), new CartItem(p, qty));
        else item.setQuantity(item.getQuantity() + qty);
        totalQuantity += qty;
        totalAmount += p.getPrice() * qty;
    }

    public void removeItem(int id) {
        CartItem item = items.get(id);
        totalQuantity -= item.getQuantity();
        totalAmount -= item.getQuantity() * item.getProduct().getPrice();
        items.remove(id);
    }

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

    public void clear() {
        items.clear();
    }

    public Collection<CartItem> getItems() {
        return items.values();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}
