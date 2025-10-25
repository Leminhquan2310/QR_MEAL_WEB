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
    private Timestamp created_at;

    public Invoice() {
    }

    public Invoice(int id, int order_id, int employee_id, double total_amount, double discount, double final_amount, PaymentMethod payment_method, Timestamp created_at) {
        this.id = id;
        this.order_id = order_id;
        this.employee_id = employee_id;
        this.total_amount = total_amount;
        this.discount = discount;
        this.final_amount = final_amount;
        this.payment_method = payment_method;
        this.created_at = created_at;
    }

    public Invoice(int order_id, int employee_id, double total_amount, double discount, double final_amount, PaymentMethod payment_method) {
        this.order_id = order_id;
        this.employee_id = employee_id;
        this.total_amount = total_amount;
        this.discount = discount;
        this.final_amount = final_amount;
        this.payment_method = payment_method;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder_d() {
        return order_id;
    }

    public void setOrder_d(int order_id) {
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
}
