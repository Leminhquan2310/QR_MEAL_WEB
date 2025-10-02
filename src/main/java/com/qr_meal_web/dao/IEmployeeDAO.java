package com.qr_meal_web.dao;

import com.qr_meal_web.model.Employee;

import java.util.List;

public interface IEmployeeDAO {
    List<Employee> selectAllEmp();

    boolean insertEmp(String name, int role, String phone, String password);

    Employee selectById(int id);

    boolean updateEmp(int id, String name, int role, String phone, String password);

    boolean deleteEmp(int id);

    Employee checkLogin(String phone, String password);

}
