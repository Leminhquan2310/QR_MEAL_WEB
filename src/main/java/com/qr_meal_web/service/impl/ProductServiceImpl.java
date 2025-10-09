package com.qr_meal_web.service.impl;

import com.qr_meal_web.model.Product;
import com.qr_meal_web.repository.ProductRepository;
import com.qr_meal_web.repository.impl.ProductRepositoryImpl;
import com.qr_meal_web.service.ProductService;

import java.util.ArrayList;
import java.util.List;

public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository = new ProductRepositoryImpl();
    @Override
    public List<Product> selectAllProduct() {
        return productRepository.selectAllProduct();
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
    public List<Product> filterProduct(String keyword, double minPrice, double maxPrice, int category, int status) {
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();

        if (!keyword.isEmpty()) {
            sql.append(" AND (p.name LIKE ? OR p.description LIKE ?)");
            params.add("%" + keyword + "%");
            params.add("%" + keyword + "%");
        }

        if (minPrice > 0) {
            sql.append(" AND price >= ?");
            params.add(minPrice);
        }

        if (maxPrice > 0) {
            sql.append(" AND price <= ?");
            params.add(maxPrice);
        }

        if (category > 0) {
            sql.append(" AND category_id = ?");
            params.add(category);
        }

        if (status >= 0) {
            sql.append(" AND p.status = ?");
            params.add(status);
        }
        return productRepository.filterProduct(sql.toString(), params);
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
}
