package com.qr_meal_web.model;

import com.qr_meal_web.enums.TableStatus;

import java.sql.Timestamp;

public class Table {
    private int id;
    private String name;
    private String qr_code_image;
    private Timestamp created_at;
    private Timestamp updated_at;
    private TableStatus status;

    public Table() {
    }

    public Table(int id, String name, String qr_code_image, Timestamp created_at, Timestamp updated_at, TableStatus status) {
        this.id = id;
        this.name = name;
        this.qr_code_image = qr_code_image;
        this.created_at = created_at;
        this.updated_at = updated_at;
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

    public String getQr_code_image() {
        return qr_code_image;
    }

    public void setQr_code_image(String qr_code_image) {
        this.qr_code_image = qr_code_image;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public TableStatus getStatus() {
        return status;
    }

    public void setStatus(TableStatus status) {
        this.status = status;
    }
}
