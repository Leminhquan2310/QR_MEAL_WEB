package com.qr_meal_web.service.impl;

import com.qr_meal_web.model.Employee;
import com.qr_meal_web.repository.EmployeeRepository;
import com.qr_meal_web.repository.impl.EmployeeRepositoryImpl;
import com.qr_meal_web.service.EmployeeService;
import com.qr_meal_web.util.Password;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository = new EmployeeRepositoryImpl();

    @Override
    public List<Employee> selectAllEmp(int limit, int page) {
        int offset = (page - 1) * limit;
        return employeeRepository.selectAllEmp(limit, offset);
    }

    @Override
    public boolean insertEmp(String name, int role, String phone, String password) {
        String passwordHashed = Password.hashPassword(password);
        return employeeRepository.insertEmp(name, role, phone, passwordHashed);
    }

    @Override
    public Employee selectById(int id) {
        return employeeRepository.selectById(id);
    }

    @Override
    public boolean updateEmp(int id, String name, int role, String phone, String password) {
        String passwordHashed = Password.hashPassword(password);
        return employeeRepository.updateEmp(id, name, role, phone, passwordHashed);
    }

    @Override
    public boolean deleteEmp(int id) {
        return employeeRepository.deleteEmp(id);
    }

    @Override
    public boolean setInactiveEmployee(int id) {
        return employeeRepository.setInactiveEmployee(id);
    }

    @Override
    public Employee checkLogin(String phone, String password) {
        String passwordHashed = employeeRepository.getPasswordHashByPhone(phone);
        boolean isLogged = Password.verifyPassword(password, passwordHashed);
        return isLogged ? employeeRepository.selectEmpByPhone(phone) : null;
    }

    @Override
    public List<Employee> filtersEmployee(String name, int role, String createdFrom, String createdTo, int limit, int page) {
        int offset = (page - 1) * limit;
        String filterString = getStringFilter(name, role, createdFrom, createdTo) + " LIMIT ? OFFSET ?";
        List<Object> params = getParamsFilter(name, role, createdFrom, createdTo);
        params.add(limit);
        params.add(offset);
        return employeeRepository.filtersEmployee(filterString, params);
    }

    @Override
    public boolean checkCanDelete(int id) {
        return employeeRepository.checkCanDelete(id);
    }

    @Override
    public int getTotalEmployees() {
        return employeeRepository.countAll();
    }

    @Override
    public int getTotalEmployeeFilter(String name, int role, String createdFrom, String createdTo) {
        String filterString = getStringFilter(name, role, createdFrom, createdTo);
        List<Object> params = getParamsFilter(name, role, createdFrom, createdTo);
        return employeeRepository.countFilter(filterString, params);
    }

    private String getStringFilter(String name, int role, String createdFrom, String createdTo) {
        StringBuilder sql = new StringBuilder();
        if (!name.isEmpty()) sql.append(" AND e.name LIKE ?");

        if (role > 0) sql.append(" AND e.role_id = ?");

        // Filter theo khoảng ngày
        if (createdFrom != null && !createdFrom.isEmpty() && createdTo != null && !createdTo.isEmpty())
            sql.append(" AND created_at BETWEEN ? AND ?");
        else if (createdFrom != null && !createdFrom.isEmpty()) sql.append(" AND created_at >= ?");
        else if (createdTo != null && !createdTo.isEmpty()) sql.append(" AND created_at <= ?");
        return sql.toString();
    }

    private List<Object> getParamsFilter(String name, int role, String createdFrom, String createdTo) {
        List<Object> params = new ArrayList<>();
        if (!name.isEmpty()) params.add("%" + name + "%");

        if (role > 0) params.add(role);

        // Filter theo khoảng ngày
        if (createdFrom != null && !createdFrom.isEmpty() && createdTo != null && !createdTo.isEmpty()) {
            params.add(Timestamp.valueOf(createdFrom + " 00:00:00"));
            params.add(Timestamp.valueOf(createdTo + " 23:59:59"));
        } else if (createdFrom != null && !createdFrom.isEmpty()) {
            params.add(Timestamp.valueOf(createdFrom + " 00:00:00"));
        } else if (createdTo != null && !createdTo.isEmpty()) {
            params.add(Timestamp.valueOf(createdTo + " 23:59:59"));
        }
        return params;
    }
}

