package com.qr_meal_web.service;

import com.qr_meal_web.model.MenuProduct;
import com.qr_meal_web.model.Product;

import java.util.List;
import java.util.Map;

public interface ProductService {
    List<Product> selectListProduct();

    List<Product> selectAllProduct(int limit, int page);

    boolean insertProduct(Product product);

    Product selectById(int id);

    boolean updateProduct(Product product);

    boolean deleteProduct(int id);

    boolean checkProductDeletable(int id);

    List<Product> filterProduct(String keyword, double minPrice, double maxPrice, int category, int status, int limit, int page);

    List<Product> selectProductForClient(int category);

    Map<Integer, List<MenuProduct>> selectMenuProductForClient();

    int getCountTotal();

    int getCountTotalProductFilter(String keyword, double minPrice, double maxPrice, int category, int status);
}
