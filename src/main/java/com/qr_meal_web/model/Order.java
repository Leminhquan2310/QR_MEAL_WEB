package com.qr_meal_web.model;

import com.qr_meal_web.enums.OrderStatus;

import java.sql.Timestamp;

public class Order {
    private int id;
    private int table_id;
    private Timestamp created_at;
    private OrderStatus status; // "pending", "serving", "done", "cancelled"

    public Order() {
    }

    public Order(int id, int table_id, Timestamp created_at, OrderStatus status) {
        this.id = id;
        this.table_id = table_id;
        this.created_at = created_at;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTable_id() {
        return table_id;
    }

    public void setTable_id(int table_id) {
        this.table_id = table_id;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
