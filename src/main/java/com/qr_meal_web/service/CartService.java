package com.qr_meal_web.service;

import com.qr_meal_web.model.CartItem;
import com.qr_meal_web.model.Product;

import java.util.Collection;

public interface CartService {
    void addItem(Product p, int qty);

    void removeItem(int id);

    void updateQuantity(int id, int delta);

    void clear();

    Collection<CartItem> getItems();

    boolean isEmpty();

    int getTotalQuantity();

    double getTotalAmount();
}
