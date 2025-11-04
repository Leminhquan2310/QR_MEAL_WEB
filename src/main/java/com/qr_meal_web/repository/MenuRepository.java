package com.qr_meal_web.repository;

import com.qr_meal_web.model.Menu;

import java.sql.Connection;
import java.util.List;

public interface MenuRepository {
    List<Menu> getAllMenus();

    Menu getMenuById(int id);

    int addMenu(Connection connection, Menu menu);

    boolean updateMenu(Connection connection, Menu menu);

    boolean deleteMenu(Connection connection, int id);
}
