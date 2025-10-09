package com.qr_meal_web.service;

import com.qr_meal_web.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> selectAllProduct();

    boolean insertProduct(Product product);

    Product selectById(int id);

    boolean updateProduct(Product product);

    boolean deleteProduct(int id);

    boolean checkProductDeletable(int id);

    List<Product> filterProduct(String keyword, double minPrice, double maxPrice, int category, int status);

    List<Product> selectProductForClient(int category);
}
