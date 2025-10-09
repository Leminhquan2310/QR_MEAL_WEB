package com.qr_meal_web.repository.impl;

import com.qr_meal_web.enums.EmployeeStatus;
import com.qr_meal_web.model.Employee;
import com.qr_meal_web.model.Role;
import com.qr_meal_web.repository.EmployeeRepository;
import com.qr_meal_web.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepositoryImpl implements EmployeeRepository {
    private Connection connection;
    private static final String SELECT_ALL = "SELECT e.*, r.name as role_name, r.color as role_color FROM employee e JOIN role r ON e.role_id = r.id";
    private static final String INSERT_EMPLOYEE = "INSERT INTO employee (name, role_id, phone, password_hash) values (?, ?, ?, ?)";
    private static final String SELECT_ONE_BY_ID = "SELECT e.*, r.name as role_name, r.color as role_color FROM employee e JOIN role r ON e.role_id = r.id WHERE e.id = ?";
    private static final String UPDATE_EMPLOYEE = "UPDATE employee SET name = ?, role_id = ?, phone = ?, password_hash = ? WHERE id = ?";
    private static final String DELETE_EMPLOYEE = "DELETE FROM employee WHERE id = ?";
    private static final String SET_INACTIVE_EMPLOYEE = "UPDATE employee SET status = 0 WHERE id = ?";
    private static final String SELECT_EMPLOYEE_BY_PHONE = "SELECT e.*, r.name as role_name, r.color as role_color FROM employee e JOIN role r ON e.role_id = r.id WHERE phone = ?";
    private static final String CHECK_CAN_DELETE = "SELECT count(*) AS result FROM activity_log WHERE employee_id = ?";
    private static final String FILTER_EMPLOYEE = "SELECT e.*, r.name as role_name, r.color as role_color FROM employee e JOIN role r ON e.role_id = r.id  WHERE 1=1";


    public EmployeeRepositoryImpl() {
        connection = DBConnection.getConnection();
    }

    @Override
    public List<Employee> selectAllEmp() {
        List<Employee> emps = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL)) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return emps;
    }

    @Override
    public boolean insertEmp(String name, int role, String phone, String passwordHashed) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_EMPLOYEE)) {
            statement.setString(1, name);
            statement.setInt(2, role);
            statement.setString(3, phone);
            statement.setString(4, passwordHashed);
            int rows = statement.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public Employee selectById(int id) {
        Employee employee = null;
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ONE_BY_ID)) {
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
            System.out.println(e.getMessage());
        }
        return employee;
    }

    @Override
    public boolean updateEmp(int id, String name, int role, String phone, String passwordHashed) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_EMPLOYEE)) {
            statement.setString(1, name);
            statement.setInt(2, role);
            statement.setString(3, phone);
            statement.setString(4, passwordHashed);
            statement.setInt(5, id);
            int result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean deleteEmp(int id) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_EMPLOYEE)) {
            statement.setInt(1, id);
            int result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean setInactiveEmployee(int id) {
        try (PreparedStatement statement = connection.prepareStatement(SET_INACTIVE_EMPLOYEE)) {
            statement.setInt(1, id);
            int result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public Employee selectEmpByPhone(String phone) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_EMPLOYEE_BY_PHONE)) {
            statement.setString(1, phone);
            ResultSet rs = statement.executeQuery();
            rs.next();
            Employee emp = new Employee();
            emp.setId(rs.getInt("id"));
            emp.setName(rs.getString("name"));
            emp.setPhone(rs.getString("phone"));
            int role_id = rs.getInt("role_id");
            String role_name = rs.getString("role_name");
            String role_color = rs.getString("role_color");
            emp.setRole(new Role(role_id, role_name, role_color));
            return emp;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public String getPasswordHashByPhone(String phone) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_EMPLOYEE_BY_PHONE)) {
            statement.setString(1, phone);
            ResultSet rs = statement.executeQuery();
            rs.next();
            return rs.getString("password_hash");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }


    @Override
    public List<Employee> filtersEmployee(String filterString, List<Object> params) {
        List<Employee> employees = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(FILTER_EMPLOYEE + filterString)) {
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
        try (PreparedStatement statement = connection.prepareStatement(CHECK_CAN_DELETE)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt("result") == 0;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
