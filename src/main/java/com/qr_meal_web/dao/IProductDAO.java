package com.qr_meal_web.dao;

import com.qr_meal_web.model.Employee;
import com.qr_meal_web.model.Product;

import java.util.List;

public interface IProductDAO {
    List<Product> selectAllProduct();

    boolean insertProduct(Product product);

    Product selectById(int id);

    boolean updateProduct(Product product);

    boolean deleteProduct(int id);

    boolean checkProductDeletable(int id);

    List<Product> filterProduct(String keyword, double minPrice, double maxPrice, int category, int status);
}
