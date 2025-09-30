package com.qr_meal_web.dao;

import com.qr_meal_web.model.Employee;
import com.qr_meal_web.model.Role;
import com.qr_meal_web.util.DBConnection;
import com.qr_meal_web.util.Password;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAOImp implements IEmployeeDAO{
    private static final String SELECT_ALL = "SELECT e.*, r.name as role_name, r.color as role_color FROM employee e JOIN role r ON e.role_id = r.id  WHERE status <> 0 ORDER BY id";
    private static final String INSERT_EMPLOYEE = "INSERT INTO employee (name, role_id, phone, password_hash) values (?, ?, ?, ?)";
    private static final String SELECT_ONE_BY_ID = "SELECT e.*, r.name as role_name, r.color as role_color FROM employee e JOIN role r ON e.role_id = r.id WHERE e.id = ?";
    private static final String UPDATE_EMPLOYEE = "UPDATE employee SET name = ?, role_id = ?, phone = ?, password_hash = ? WHERE id = ?";
    private static final String DELETE_EMPLOYEE = "UPDATE employee SET status = 0 WHERE id = ?";
    private static final String SELECT_EMPLOYEE_LOGIN = "SELECT e.*, r.name as role_name, r.color as role_color FROM employee e JOIN role r ON e.role_id = r.id WHERE phone = ?";

    @Override
    public List<Employee> selectAllEmp() {
        List<Employee> emps = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(SELECT_ALL)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int role_id = rs.getInt("role_id");
                String role_name = rs.getString("role_name");
                String role_color = rs.getString("role_color");
                Role role = new Role(role_id, role_name, role_color);
                String phone = rs.getString("phone");
                Timestamp create_at = rs.getTimestamp("created_at");
                int status = rs.getInt("status");
                emps.add(new Employee(id, name, role, phone, create_at, status));
            }
            return emps;
        } catch (SQLException e) {
           e.printStackTrace();
           return null;
        }
    }

    @Override
    public boolean insertEmp(String name, int role, String phone, String password) {
        String hashed_password = Password.hashPassword(password);
        boolean isSuccess;
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(INSERT_EMPLOYEE)){
            statement.setString(1, name);
            statement.setInt(2, role);
            statement.setString(3, phone);
            statement.setString(4, hashed_password);
            int rows = statement.executeUpdate();  // ✅ dùng executeUpdate
            isSuccess = rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            isSuccess = false;
        }
        return isSuccess;
    }

    @Override
    public Employee selectById(int id) {
        Employee employee = null;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ONE_BY_ID)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                int emp_id = rs.getInt("id");
                String name = rs.getString("name");
                int role_id = rs.getInt("role_id");
                String role_name = rs.getString("role_name");
                String role_color = rs.getString("role_color");
                Role role = new Role(role_id, role_name, role_color);
                String phone = rs.getString("phone");
                Timestamp create_at = rs.getTimestamp("created_at");
                int status = rs.getInt("status");
                employee = new Employee(emp_id, name, role, phone, create_at, status);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return employee;
    }

    @Override
    public boolean updateEmp(int id, String name, int role, String phone, String password) {
        String hashedPassword = Password.hashPassword(password);
        boolean isSuccess;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_EMPLOYEE)){
            statement.setString(1, name);
            statement.setInt(2, role);
            statement.setString(3, phone);
            statement.setString(4, hashedPassword);
            statement.setInt(5, id);
            int result = statement.executeUpdate();
            isSuccess = result > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return isSuccess;
    }

    @Override
    public boolean deleteEmp(int id) {
        boolean isSuccess;
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_EMPLOYEE)){
            statement.setInt(1, id);
            int result = statement.executeUpdate();
            isSuccess = result > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return isSuccess;
    }

    @Override
    public Employee checkLogin(String phone, String password) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_EMPLOYEE_LOGIN)) {
            statement.setString(1, phone);
            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                String hashed_password = rs.getString("password_hash");
                if (Password.verifyPassword(password, hashed_password)){
                    Employee emp = new Employee();
                    emp.setId(rs.getInt("id"));
                    emp.setName(rs.getString("name"));
                    emp.setPhone(rs.getString("phone"));
                    int role_id = rs.getInt("role_id");
                    String role_name = rs.getString("role_name");
                    String role_color = rs.getString("role_color");
                    emp.setRole(new Role(role_id, role_name, role_color));
                    return emp;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
