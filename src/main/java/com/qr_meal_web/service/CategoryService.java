package com.qr_meal_web.service;

import com.qr_meal_web.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> selectListCategory();

    List<Category> selectAllCategory(int limit, int page);

    boolean insertCategory(String name, String description, String icon);

    Category selectCategory(int id);

    boolean updateCategory(int id, String name, String desc, String icon);

    boolean deleteCategory(int id);

    List<Category> filterCategory(String keyword, int status, String createdFrom, String createdTo, int limit, int page);

    int getCountTotal();

    int getCountTotalCategoryFilter(String keyword, int status, String createdFrom, String createdTo);
}
