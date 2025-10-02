package com.qr_meal_web.dao;

import com.qr_meal_web.model.Category;

import java.util.List;

public interface ICategoryDAO {
    List<Category> selectAllCategory();

    boolean insertCategory(String name, String description, String icon);

    Category selectCategory(int id);

    boolean updateCategory(int id, String name, String desc, String icon);

    boolean deleteCategory(int id);

    List<Category> filterCategory(String name, int status, String createdFrom, String createdTo);
}
