package com.qr_meal_web.model;

import com.qr_meal_web.enums.EmployeeStatus;

import java.sql.Timestamp;
import java.util.Date;

public class Employee {
    private int id;
    private String name;
    private Role role;
    private String phone;
    private Timestamp created_at;
    private EmployeeStatus status;

    public Employee() {
    }

    public Employee(int id, String name, Role role, String phone, Timestamp created_at, EmployeeStatus status) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.phone = phone;
        this.created_at = created_at;
        this.status = status;
    }

    public EmployeeStatus getStatus() {
        return status;
    }

    public void setStatus(EmployeeStatus status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
}
