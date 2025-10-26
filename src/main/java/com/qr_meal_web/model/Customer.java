package com.qr_meal_web.model;

import java.sql.Timestamp;

public class Customer {
    private int id;
    private String name;
    private String phone;
    private int points;
    private Timestamp created_at;

    public Customer() {
    }

    public Customer(int id, String name, String phone, int points, Timestamp created_at) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.points = points;
        this.created_at = created_at;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
}
