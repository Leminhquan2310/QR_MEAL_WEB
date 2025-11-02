package com.qr_meal_web.model;

import com.qr_meal_web.enums.PaymentMethod;

import java.sql.Timestamp;

public class Invoice {
    private int id;
    private int order_id;
    private int employee_id;
    private double total_amount;
    private double discount;
    private double final_amount;
    private PaymentMethod payment_method;
    private int discount_id;
    private int points_used;
    private Timestamp created_at;

    public Invoice() {
    }

    public Invoice(int id, int order_id, int employee_id, double total_amount, double discount, double final_amount, PaymentMethod payment_method,int discount_id, int points_used, Timestamp created_at) {
        this.id = id;
        this.order_id = order_id;
        this.employee_id = employee_id;
        this.total_amount = total_amount;
        this.discount = discount;
        this.final_amount = final_amount;
        this.payment_method = payment_method;
        this.discount_id = discount_id;
        this.points_used = points_used;
        this.created_at = created_at;
    }

    public Invoice(int order_id, int employee_id, double total_amount, double discount, double final_amount, PaymentMethod payment_method, int discount_id, int points_used) {
        this.order_id = order_id;
        this.employee_id = employee_id;
        this.total_amount = total_amount;
        this.discount = discount;
        this.final_amount = final_amount;
        this.payment_method = payment_method;
        this.discount_id = discount_id;
        this.points_used = points_used;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getFinal_amount() {
        return final_amount;
    }

    public void setFinal_amount(double final_amount) {
        this.final_amount = final_amount;
    }

    public PaymentMethod getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(PaymentMethod payment_method) {
        this.payment_method = payment_method;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public int getDiscount_id() {
        return discount_id;
    }

    public void setDiscount_id(int discount_id) {
        this.discount_id = discount_id;
    }

    public int getPoints_used() {
        return points_used;
    }

    public void setPoints_used(int points_used) {
        this.points_used = points_used;
    }
}
