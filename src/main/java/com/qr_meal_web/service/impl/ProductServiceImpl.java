package com.qr_meal_web.service.impl;

import com.qr_meal_web.model.MenuProduct;
import com.qr_meal_web.model.Product;
import com.qr_meal_web.repository.ProductRepository;
import com.qr_meal_web.repository.impl.ProductRepositoryImpl;
import com.qr_meal_web.service.ProductService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository = new ProductRepositoryImpl();

    @Override
    public List<Product> selectListProduct() {
        return productRepository.selectListProduct();
    }

    @Override
    public List<Product> selectAllProduct(int limit, int page) {
        int offset = (page - 1) * limit;
        return productRepository.selectAllProduct(limit, offset);
    }

    @Override
    public boolean insertProduct(Product product) {
        return productRepository.insertProduct(product);
    }

    @Override
    public Product selectById(int id) {
        return productRepository.selectById(id);
    }

    @Override
    public boolean updateProduct(Product product) {
        return productRepository.updateProduct(product);
    }

    @Override
    public boolean deleteProduct(int id) {
        return productRepository.deleteProduct(id);
    }

    @Override
    public boolean checkProductDeletable(int id) {
        return productRepository.checkProductDeletable(id);
    }

    @Override
    public List<Product> filterProduct(String keyword, double minPrice, double maxPrice, int category, int status, int limit, int page) {
        String sql = getStringFilter(keyword, minPrice, maxPrice, category, status) +  " LIMIT ? OFFSET ?";
        List<Object> params = getParamsFilter(keyword, minPrice, maxPrice, category, status);
        int offset = (page - 1) * limit;
        params.add(limit);
        params.add(offset);
        return productRepository.filterProduct(sql, params);
    }

    @Override
    public List<Product> selectProductForClient(int category) {
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();

        if (category >= 0) {
            sql.append(" AND category_id = ?");
            params.add(category);
        }
        return productRepository.selectProductForClient(sql.toString(), params);
    }

    @Override
    public Map<Integer, List<MenuProduct>> selectMenuProductForClient() {
        return productRepository.selectMenuProductForClient();
    }

    @Override
    public int getCountTotal() {
        return productRepository.countAll();
    }

    @Override
    public int getCountTotalProductFilter(String keyword, double minPrice, double maxPrice, int category, int status) {
        String sql = getStringFilter(keyword, minPrice, maxPrice, category, status);
        List<Object> params = getParamsFilter(keyword, minPrice, maxPrice, category, status);
        return productRepository.countFilter(sql, params);
    }

    private String getStringFilter(String keyword, double minPrice, double maxPrice, int category, int status) {
        StringBuilder sql = new StringBuilder();
        if (!keyword.isEmpty()) sql.append(" AND (p.name LIKE ? OR p.description LIKE ?)");
        if (minPrice > 0) sql.append(" AND price >= ?");
        if (maxPrice > 0) sql.append(" AND price <= ?");
        if (category > 0) sql.append(" AND category_id = ?");
        if (status >= 0) sql.append(" AND p.status = ?");
        return sql.toString();
    }

    private List<Object> getParamsFilter(String keyword, double minPrice, double maxPrice, int category, int status) {
        List<Object> params = new ArrayList<>();
        if (!keyword.isEmpty()) {
            params.add("%" + keyword + "%");
            params.add("%" + keyword + "%");
        }
        if (minPrice > 0) params.add(minPrice);
        if (maxPrice > 0) params.add(maxPrice);
        if (category > 0) params.add(category);
        if (status >= 0) params.add(status);
        return params;
    }
}
