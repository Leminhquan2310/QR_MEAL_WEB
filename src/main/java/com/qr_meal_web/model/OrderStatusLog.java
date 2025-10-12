package com.qr_meal_web.model;

import com.qr_meal_web.enums.OrderStatus;

import java.sql.Timestamp;

public class OrderStatusLog {
    private int id;
    private int orderId;
    private OrderStatus old_status;
    private OrderStatus new_status;
    private Employee changed_by;
    private Timestamp changed_at;
    private String note;

    public OrderStatusLog() {
    }

    public OrderStatusLog(int id, int orderId, OrderStatus old_status, OrderStatus new_status, Employee changed_by, Timestamp changed_at, String note) {
        this.id = id;
        this.orderId = orderId;
        this.old_status = old_status;
        this.new_status = new_status;
        this.changed_by = changed_by;
        this.changed_at = changed_at;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public OrderStatus getOld_status() {
        return old_status;
    }

    public void setOld_status(OrderStatus old_status) {
        this.old_status = old_status;
    }

    public OrderStatus getNew_status() {
        return new_status;
    }

    public void setNew_status(OrderStatus new_status) {
        this.new_status = new_status;
    }

    public Employee getChanged_by() {
        return changed_by;
    }

    public void setChanged_by(Employee changed_by) {
        this.changed_by = changed_by;
    }

    public Timestamp getChanged_at() {
        return changed_at;
    }

    public void setChanged_at(Timestamp changed_at) {
        this.changed_at = changed_at;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
