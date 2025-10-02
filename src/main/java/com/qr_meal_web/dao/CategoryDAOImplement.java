package com.qr_meal_web.dao;

import com.qr_meal_web.model.Category;
import com.qr_meal_web.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAOImplement implements ICategoryDAO {
    private static final String SELECT_ALL_CATEGORY = "SELECT * FROM category WHERE status = 1 ORDER BY id";
    private static final String INSERT_CATEGORY = "INSERT  INTO category (name, description, icon) values (?, ?, ?)";
    private static final String SELECT_CATEGORY = "SELECT * FROM category WHERE id = ?";
    private static final String UPDATE_CATEGORY = "UPDATE category SET name = ?, description = ?, icon = ? WHERE id = ?";
    private static final String DELETE_CATEGORY = "UPDATE category SET status = 0 WHERE id = ?";
    private static final String FILTERS_CATEGORY = "SELECT * FROM category WHERE  name LIKE ? AND status = ? AND DATE(created_at) BETWEEN ? AND ?";

    @Override
    public List<Category> selectAllCategory() {
        List<Category> categories = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(SELECT_ALL_CATEGORY)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String desc = rs.getString("description");
                String icon = rs.getString("icon");
                int status = rs.getInt("status");
                Timestamp created_at = rs.getTimestamp("created_at");
                categories.add(new Category(id, name, desc, icon, status, created_at));
            }
            return categories;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean insertCategory(String name, String description, String icon) {
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(INSERT_CATEGORY)) {
            statement.setString(1, name);
            statement.setString(2, description);
            statement.setString(3, icon);
            int result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Category selectCategory(int id) {
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(SELECT_CATEGORY)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int id_cate = rs.getInt("id");
                String name = rs.getString("name");
                String desc = rs.getString("description");
                String icon = rs.getString("icon");
                int status = rs.getInt("status");
                Timestamp created_at = rs.getTimestamp("created_at");
                return new Category(id_cate, name, desc, icon, status, created_at);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateCategory(int id, String name, String desc, String icon) {
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_CATEGORY)) {
            statement.setString(1, name);
            statement.setString(2, desc);
            statement.setString(3, icon);
            statement.setInt(4, id);
            int rs = statement.executeUpdate();
            return rs > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteCategory(int id) {
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_CATEGORY)) {
            statement.setInt(1, id);
            int rs = statement.executeUpdate();
            return rs > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Category> filterCategory(String name, int status, String createdFrom, String createdTo) {
        List<Category> categories = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM category WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            sql.append(" AND name LIKE ?");
            params.add("%" + name + "%");
        }

        if (status != 2) {
            sql.append(" AND status = ?");
            params.add(status);
        }

        // Filter theo khoảng ngày
        if (createdFrom != null && !createdFrom.isEmpty() && createdTo != null && !createdTo.isEmpty()) {
            sql.append(" AND created_at BETWEEN ? AND ?");
            params.add(Timestamp.valueOf(createdFrom + " 00:00:00"));
            params.add(Timestamp.valueOf(createdTo + " 23:59:59"));
        } else if (createdFrom != null && !createdFrom.isEmpty()) {
            sql.append(" AND created_at >= ?");
            params.add(Timestamp.valueOf(createdFrom + " 00:00:00"));
        } else if (createdTo != null && !createdTo.isEmpty()) {
            sql.append(" AND created_at <= ?");
            params.add(Timestamp.valueOf(createdTo + " 23:59:59"));
        }


        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String cate_name = rs.getString("name");
                String desc = rs.getString("description");
                String icon = rs.getString("icon");
                int cate_status = rs.getInt("status");
                Timestamp created_at = rs.getTimestamp("created_at");
                categories.add(new Category(id, cate_name, desc,  icon, cate_status, created_at));
            }
            System.out.println(categories.size());
            return categories;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
