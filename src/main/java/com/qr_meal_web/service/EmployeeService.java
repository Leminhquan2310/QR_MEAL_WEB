package com.qr_meal_web.service;

import com.qr_meal_web.model.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> selectAllEmp(int limit, int page);

    boolean insertEmp(String name, int role, String phone, String password);

    Employee selectById(int id);

    boolean updateEmp(int id, String name, int role, String phone, String password);

    boolean deleteEmp(int id);

    boolean setInactiveEmployee(int id);

    Employee checkLogin(String phone, String password);

    List<Employee> filtersEmployee(String name, int role, String createdFrom, String createdTo, int limit, int page);

    boolean checkCanDelete(int id);

    int getTotalEmployees();

    int getTotalEmployeeFilter(String name, int role, String createdFrom, String createdTo);
}
