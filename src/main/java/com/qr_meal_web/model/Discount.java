package com.qr_meal_web.model;

import com.qr_meal_web.enums.DiscountStatus;
import com.qr_meal_web.enums.DiscountType;

import java.sql.Timestamp;

public class Discount {
    private int id;
    private int points_required;
    private String description;
    private Timestamp created_at;
    private double discount_value;
    private DiscountType discount_type;
    private DiscountStatus status;

    public Discount() {
    }

    public Discount(int id, int points_required, String description, Timestamp created_at, double discount_value, DiscountType discount_type, DiscountStatus status) {
        this.id = id;
        this.points_required = points_required;
        this.description = description;
        this.created_at = created_at;
        this.discount_value = discount_value;
        this.discount_type = discount_type;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPoints_required() {
        return points_required;
    }

    public void setPoints_required(int points_required) {
        this.points_required = points_required;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public double getDiscount_value() {
        return discount_value;
    }

    public void setDiscount_value(double discount_value) {
        this.discount_value = discount_value;
    }

    public DiscountType getDiscount_type() {
        return discount_type;
    }

    public void setDiscount_type(DiscountType discount_type) {
        this.discount_type = discount_type;
    }

    public DiscountStatus getStatus() {
        return status;
    }

    public void setStatus(DiscountStatus status) {
        this.status = status;
    }
}
