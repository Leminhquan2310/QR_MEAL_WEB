package com.qr_meal_web.repository.impl;

import com.qr_meal_web.model.Role;
import com.qr_meal_web.repository.RoleRepository;
import com.qr_meal_web.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleRepositoryImpl implements RoleRepository {
    private final Connection connection = DBConnection.getConnection();
    private static final String SELECT_ALL = "SELECT * FROM role";

    @Override
    public List<Role> selectAllRole() {
        List<Role> roles = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String color = rs.getString("color");
                roles.add(new Role(id, name, color));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return roles;
    }
}
