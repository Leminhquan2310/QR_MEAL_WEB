package com.qr_meal_web.model;

import java.sql.Timestamp;
import java.util.Date;

public class Employee {
    private int id;
    private String name;
    private Role role;
    private String phone;
    private Timestamp created_at;
    private int status;

    public Employee() {
    }

    public Employee(int id, String name, Role role, String phone, Timestamp created_at, int status) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.phone = phone;
        this.created_at = created_at;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
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
