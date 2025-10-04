package com.qr_meal_web.dao;

import com.qr_meal_web.enums.EmployeeStatus;
import com.qr_meal_web.enums.ProductStatus;
import com.qr_meal_web.model.Category;
import com.qr_meal_web.model.Employee;
import com.qr_meal_web.model.Product;
import com.qr_meal_web.model.Role;
import com.qr_meal_web.util.DBConnection;
import com.qr_meal_web.util.Password;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAOImplement implements IEmployeeDAO {
    private static final String SELECT_ALL = "SELECT e.*, r.name as role_name, r.color as role_color FROM employee e JOIN role r ON e.role_id = r.id";
    private static final String INSERT_EMPLOYEE = "INSERT INTO employee (name, role_id, phone, password_hash) values (?, ?, ?, ?)";
    private static final String SELECT_ONE_BY_ID = "SELECT e.*, r.name as role_name, r.color as role_color FROM employee e JOIN role r ON e.role_id = r.id WHERE e.id = ?";
    private static final String UPDATE_EMPLOYEE = "UPDATE employee SET name = ?, role_id = ?, phone = ?, password_hash = ? WHERE id = ?";
    private static final String DELETE_EMPLOYEE = "DELETE FROM employee WHERE id = ?";
    private static final String SET_INACTIVE_EMPLOYEE = "UPDATE employee SET status = 0 WHERE id = ?";
    private static final String SELECT_EMPLOYEE_LOGIN = "SELECT e.*, r.name as role_name, r.color as role_color FROM employee e JOIN role r ON e.role_id = r.id WHERE phone = ?";
    private static final String CHECK_CAN_DELETE = "SELECT count(*) AS result FROM activity_log WHERE employee_id = ?";

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
                emps.add(new Employee(id, name, role, phone, create_at, EmployeeStatus.fromCode(status)));
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
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(INSERT_EMPLOYEE)) {
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
            while (rs.next()) {
                int emp_id = rs.getInt("id");
                String name = rs.getString("name");
                int role_id = rs.getInt("role_id");
                String role_name = rs.getString("role_name");
                String role_color = rs.getString("role_color");
                Role role = new Role(role_id, role_name, role_color);
                String phone = rs.getString("phone");
                Timestamp create_at = rs.getTimestamp("created_at");
                int status = rs.getInt("status");
                employee = new Employee(emp_id, name, role, phone, create_at, EmployeeStatus.fromCode(status));
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
             PreparedStatement statement = connection.prepareStatement(UPDATE_EMPLOYEE)) {
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
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_EMPLOYEE)) {
            statement.setInt(1, id);
            int result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean setInactiveEmployee(int id) {
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(SET_INACTIVE_EMPLOYEE)) {
            statement.setInt(1, id);
            int result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Employee checkLogin(String phone, String password) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_EMPLOYEE_LOGIN)) {
            statement.setString(1, phone);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String hashed_password = rs.getString("password_hash");
                if (Password.verifyPassword(password, hashed_password)) {
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

    @Override
    public List<Employee> filtersEmployee(String name, int role, String createdFrom, String createdTo) {
        List<Employee> employees = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT e.*, r.name as role_name, r.color as role_color FROM employee e JOIN role r ON e.role_id = r.id  WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (!name.isEmpty()) {
            sql.append(" AND e.name LIKE ?");
            params.add("%" + name + "%");
        }

        if (role > 0) {
            sql.append(" AND e.role_id = ?");
            params.add(role);
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
                String e_name = rs.getString("name");
                int role_id = rs.getInt("role_id");
                String role_name = rs.getString("role_name");
                String role_color = rs.getString("role_color");
                Role e_role = new Role(role_id, role_name, role_color);
                String phone = rs.getString("phone");
                Timestamp create_at = rs.getTimestamp("created_at");
                int status = rs.getInt("status");
                employees.add(new Employee(id, e_name, e_role, phone, create_at, EmployeeStatus.fromCode(status)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    @Override
    public boolean checkCanDelete(int id) {
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(CHECK_CAN_DELETE)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt("result") == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
