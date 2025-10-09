package com.qr_meal_web.service.impl;

import com.qr_meal_web.model.Category;
import com.qr_meal_web.repository.CategoryRepository;
import com.qr_meal_web.repository.impl.CategoryRepositoryImpl;
import com.qr_meal_web.service.CategoryService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    private CategoryRepository categoryRepository = new CategoryRepositoryImpl();

    @Override
    public List<Category> selectAllCategory() {
        return categoryRepository.selectAllCategory();
    }

    @Override
    public boolean insertCategory(String name, String description, String icon) {
        return categoryRepository.insertCategory(name, description, icon);
    }

    @Override
    public Category selectCategory(int id) {
        return categoryRepository.selectCategory(id);
    }

    @Override
    public boolean updateCategory(int id, String name, String desc, String icon) {
        return categoryRepository.updateCategory(id, name, desc, icon);
    }

    @Override
    public boolean deleteCategory(int id) {
        return categoryRepository.deleteCategory(id);
    }

    @Override
    public List<Category> filterCategory(String keyword, int status, String createdFrom, String createdTo) {
        StringBuilder filterString = new StringBuilder();
        List<Object> params = new ArrayList<>();
        if (keyword != null && !keyword.isEmpty()) {
            filterString.append(" AND (name LIKE ? OR description LIKE ?)");
            params.add("%" + keyword + "%");
            params.add("%" + keyword + "%");
        }

        if (status != 2) {
            filterString.append(" AND status = ?");
            params.add(status);
        }

        // Filter theo khoảng ngày
        if (createdFrom != null && !createdFrom.isEmpty() && createdTo != null && !createdTo.isEmpty()) {
            filterString.append(" AND created_at BETWEEN ? AND ?");
            params.add(Timestamp.valueOf(createdFrom + " 00:00:00"));
            params.add(Timestamp.valueOf(createdTo + " 23:59:59"));
        } else if (createdFrom != null && !createdFrom.isEmpty()) {
            filterString.append(" AND created_at >= ?");
            params.add(Timestamp.valueOf(createdFrom + " 00:00:00"));
        } else if (createdTo != null && !createdTo.isEmpty()) {
            filterString.append(" AND created_at <= ?");
            params.add(Timestamp.valueOf(createdTo + " 23:59:59"));
        }

        return categoryRepository.filterCategory(filterString.toString(), params);
    }
}
