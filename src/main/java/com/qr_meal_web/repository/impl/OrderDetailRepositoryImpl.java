package com.qr_meal_web.repository.impl;

import com.qr_meal_web.enums.ProductStatus;
import com.qr_meal_web.model.Category;
import com.qr_meal_web.model.OrderDetail;
import com.qr_meal_web.model.Product;
import com.qr_meal_web.repository.OrderDetailRepository;
import com.qr_meal_web.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailRepositoryImpl implements OrderDetailRepository {
    private static final String SELECT_ORDER_DETAIL_BY_ORDER_ID =
            "SELECT od.order_id, od.quantity, od.price as unit_price, p.*, c.id AS c_id, c.name AS c_name, c.icon AS c_icon " +
                    "FROM orderdetail od " +
                    "JOIN product p ON od.product_id = p.id " +
                    "JOIN category c ON p.category_id = c.id WHERE od.order_id = ?";
    private static final String SELECT_ORDER_DETAIL_BY_Table_ID =
            "SELECT od.order_id, od.quantity, od.price as unit_price, p.*, c.id AS c_id, c.name AS c_name, c.icon AS c_icon " +
                    "FROM `order` o JOIN orderdetail od ON o.id = od.order_id " +
                    "JOIN product p ON od.product_id = p.id " +
                    "JOIN category c ON p.category_id = c.id WHERE o.table_id = ? AND o.status NOT IN (3, 4)";

    @Override
    public List<OrderDetail> selectOrderDetailByOrderId(int id) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ORDER_DETAIL_BY_ORDER_ID)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int order_id = rs.getInt("order_id");
                int quantity = rs.getInt("quantity");
                double unit_price = rs.getDouble("unit_price");

                int pro_id = rs.getInt("id");
                String name = rs.getString("name");
                String desc = rs.getString("description");
                double price = rs.getDouble("price");
                int statusCode = rs.getInt("status");
                Category cate = new Category();
                cate.setId(rs.getInt("c_id"));
                cate.setName(rs.getString("c_name"));
                cate.setIcon(rs.getString("c_icon"));
                String image = rs.getString("image");
                int cooking_time = rs.getInt("cooking_time");
                Product product = new Product(pro_id, name, desc, price, ProductStatus.fromCode(statusCode), cate, image, cooking_time);
                orderDetails.add(new OrderDetail(order_id, pro_id, quantity, unit_price, product));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return orderDetails;
    }

    @Override
    public List<OrderDetail> selectOrderDetailByTableId(int id) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ORDER_DETAIL_BY_Table_ID)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int order_id = rs.getInt("order_id");
                int quantity = rs.getInt("quantity");
                double unit_price = rs.getDouble("unit_price");

                int pro_id = rs.getInt("id");
                String name = rs.getString("name");
                String desc = rs.getString("description");
                double price = rs.getDouble("price");
                int statusCode = rs.getInt("status");
                Category cate = new Category();
                cate.setId(rs.getInt("c_id"));
                cate.setName(rs.getString("c_name"));
                cate.setIcon(rs.getString("c_icon"));
                String image = rs.getString("image");
                int cooking_time = rs.getInt("cooking_time");
                Product product = new Product(pro_id, name, desc, price, ProductStatus.fromCode(statusCode), cate, image, cooking_time);
                orderDetails.add(new OrderDetail(order_id, pro_id, quantity, unit_price, product));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return orderDetails;
    }

}
