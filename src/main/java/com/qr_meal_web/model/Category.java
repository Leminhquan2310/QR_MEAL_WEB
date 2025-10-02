package com.qr_meal_web.model;

import java.sql.Timestamp;

public class Category {
    private int id;
    private String name;
    private String description;
    private String icon;
    private Timestamp created_at;
    private int status;

    public Category() {
    }

    public Category(int id, String name, String description, String icon, int status, Timestamp create_at) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.status = status;
        this.created_at = create_at;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
}
