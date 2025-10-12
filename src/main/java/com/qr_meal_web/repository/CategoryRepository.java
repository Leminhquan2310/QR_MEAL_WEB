package com.qr_meal_web.repository;

import com.qr_meal_web.model.Category;

import java.util.List;

public interface CategoryRepository {
    List<Category> selectListCategory();

    List<Category> selectAllCategory(int limit, int offset);

    boolean insertCategory(String name, String description, String icon);

    Category selectCategory(int id);

    boolean updateCategory(int id, String name, String desc, String icon);

    boolean deleteCategory(int id);

    List<Category> filterCategory(String filterString, List<Object> params);

    int countAll();

    int countFilter(String filterString, List<Object> params);
}
