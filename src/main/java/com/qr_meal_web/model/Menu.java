package com.qr_meal_web.model;

import com.qr_meal_web.enums.MenuStatus;

import java.sql.Timestamp;

public class Menu {
    private int id;
    private String name;
    private String description;
    private MenuStatus status;
    private Timestamp created_at;

    public Menu() {
    }

    public Menu(int id, String name, String description, MenuStatus status, Timestamp created_at) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.created_at = created_at;
    }

    // Getter - Setter
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

    public MenuStatus getStatus() {
        return status;
    }

    public void setStatus(MenuStatus status) {
        this.status = status;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
}
