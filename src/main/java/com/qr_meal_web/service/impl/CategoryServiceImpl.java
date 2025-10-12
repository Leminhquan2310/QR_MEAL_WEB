package com.qr_meal_web.service.impl;

import com.qr_meal_web.model.Category;
import com.qr_meal_web.repository.CategoryRepository;
import com.qr_meal_web.repository.impl.CategoryRepositoryImpl;
import com.qr_meal_web.service.CategoryService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository = new CategoryRepositoryImpl();


    @Override
    public List<Category> selectListCategory() {
        return categoryRepository.selectListCategory();
    }

    @Override
    public List<Category> selectAllCategory(int limit, int page) {
        int offset = (page - 1) * limit;
        return categoryRepository.selectAllCategory(limit, offset);
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
    public List<Category> filterCategory(String keyword, int status, String createdFrom, String createdTo, int limit, int page) {
        String filterString = getStringFilter(keyword, status, createdFrom, createdTo) + " LIMIT ? OFFSET ?";
        List<Object> params = getParamsFilter(keyword, status, createdFrom, createdTo);
        int offset = (page - 1) * limit;
        params.add(limit);
        params.add(offset);
        return categoryRepository.filterCategory(filterString, params);
    }

    @Override
    public int getCountTotal() {
        return categoryRepository.countAll();
    }

    @Override
    public int getCountTotalCategoryFilter(String keyword, int status, String createdFrom, String createdTo) {
        String sql = getStringFilter(keyword, status, createdFrom, createdTo);
        List<Object> params = getParamsFilter(keyword, status, createdFrom, createdTo);
        return categoryRepository.countFilter(sql, params);
    }

    private String getStringFilter(String keyword, int status, String createdFrom, String createdTo) {
        StringBuilder sql = new StringBuilder();
        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND (name LIKE ? OR description LIKE ?)");
        }
        if (status != -1) {
            sql.append(" AND status = ?");
        }
        // Filter theo khoảng ngày
        if (createdFrom != null && !createdFrom.isEmpty() && createdTo != null && !createdTo.isEmpty())
            sql.append(" AND created_at BETWEEN ? AND ?");
        else if (createdFrom != null && !createdFrom.isEmpty()) sql.append(" AND created_at >= ?");
        else if (createdTo != null && !createdTo.isEmpty()) sql.append(" AND created_at <= ?");
        return sql.toString();
    }

    private List<Object> getParamsFilter(String keyword, int status, String createdFrom, String createdTo) {
        List<Object> params = new ArrayList<>();
        if (keyword != null && !keyword.isEmpty()) {
            params.add("%" + keyword + "%");
            params.add("%" + keyword + "%");
        }
        if (status != -1) {
            params.add(status);
        }
        // Filter theo khoảng ngày
        if (createdFrom != null && !createdFrom.isEmpty() && createdTo != null && !createdTo.isEmpty()) {
            params.add(Timestamp.valueOf(createdFrom + " 00:00:00"));
            params.add(Timestamp.valueOf(createdTo + " 23:59:59"));
        } else if (createdFrom != null && !createdFrom.isEmpty()) {
            params.add(Timestamp.valueOf(createdFrom + " 00:00:00"));
        } else if (createdTo != null && !createdTo.isEmpty()) {
            params.add(Timestamp.valueOf(createdTo + " 23:59:59"));
        }
        return params;
    }
}
