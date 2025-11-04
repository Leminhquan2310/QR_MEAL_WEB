package com.qr_meal_web.repository;

import com.qr_meal_web.model.MenuProduct;

import java.sql.Connection;
import java.util.List;

public interface MenuProductRepository {
    List<MenuProduct> findById(int menu_id);

    void addMenuProduct(Connection connection, int menu_id, int product_id);

    void deleteMenuProduct(Connection connection, int menu_id);

}
