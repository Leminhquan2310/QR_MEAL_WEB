package com.qr_meal_web.repository;

import com.qr_meal_web.model.Employee;

import java.util.List;

public interface EmployeeRepository {
    List<Employee> selectAllEmp(int limit, int offset);

    boolean insertEmp(String name, int role, String phone, String passwordHashed);

    Employee selectById(int id);

    boolean updateEmp(int id, String name, int role, String phone, String password);

    boolean deleteEmp(int id);

    boolean setInactiveEmployee(int id);

    Employee selectEmpByPhone(String phone);

    String getPasswordHashByPhone(String phone);

    List<Employee> filtersEmployee(String filterString, List<Object> params);

    boolean checkCanDelete(int id);

    int countAll();

    int countFilter(String filterString, List<Object> params);
}
