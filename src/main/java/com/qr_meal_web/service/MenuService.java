package com.qr_meal_web.service;

import com.qr_meal_web.model.Menu;
import com.qr_meal_web.model.MenuProduct;

import java.util.List;

public interface MenuService {
    List<Menu> findAll();

    List<MenuProduct> findByMenuId(int menuId);

    boolean create(Menu menu, List<Integer> products);

    boolean update(Menu menu, List<Integer> products);

    boolean delete(int menu_id);
}
