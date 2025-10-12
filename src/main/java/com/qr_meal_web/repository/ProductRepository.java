package com.qr_meal_web.repository;

import com.qr_meal_web.model.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> selectAllProduct(int limit, int offset);

    boolean insertProduct(Product product);

    Product selectById(int id);

    boolean updateProduct(Product product);

    boolean deleteProduct(int id);

    boolean checkProductDeletable(int id);

    List<Product> filterProduct(String filterString, List<Object> params);

    List<Product> selectProductForClient(String filterString, List<Object> params);

    int countAll();

    int countFilter(String filterString, List<Object> params);
}
