package com.qr_meal_web.service.impl;

import com.qr_meal_web.model.Menu;
import com.qr_meal_web.model.MenuProduct;
import com.qr_meal_web.repository.MenuProductRepository;
import com.qr_meal_web.repository.MenuRepository;
import com.qr_meal_web.repository.impl.MenuProductRepositoryImpl;
import com.qr_meal_web.repository.impl.MenuRepositoryImpl;
import com.qr_meal_web.service.MenuService;
import com.qr_meal_web.util.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepo = new MenuRepositoryImpl();
    private final MenuProductRepository menuProductRepository = new MenuProductRepositoryImpl();

    @Override
    public List<Menu> findAll() {
        return menuRepo.getAllMenus();
    }

    @Override
    public List<MenuProduct> findByMenuId(int menuId) {
        MenuProductRepository menuProductRepository = new MenuProductRepositoryImpl();
        return menuProductRepository.findById(menuId);
    }

    @Override
    public boolean create(Menu menu, List<Integer> products) {
        Connection connection = DBConnection.getConnection();
        try {
            connection.setAutoCommit(false);

            int menu_id = menuRepo.addMenu(connection, menu);

            for (Integer id : products) {
                menuProductRepository.addMenuProduct(connection, menu_id, id);
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            System.out.println(e.getMessage());
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean update(Menu menu, List<Integer> products) {
        Connection connection = DBConnection.getConnection();
        try {
            connection.setAutoCommit(false);

            menuRepo.updateMenu(connection, menu);

            menuProductRepository.deleteMenuProduct(connection, menu.getId());
            for (Integer id : products) {
                menuProductRepository.addMenuProduct(connection, menu.getId(), id);
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            System.out.println(e.getMessage());
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean delete(int menu_id) {
        Connection connection = DBConnection.getConnection();
        try {
            connection.setAutoCommit(false);
            menuProductRepository.deleteMenuProduct(connection, menu_id);
            menuRepo.deleteMenu(connection, menu_id);
            connection.commit();
            return true;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            System.out.println(e.getMessage());
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
