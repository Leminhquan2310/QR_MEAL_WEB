package com.qr_meal_web.model;

import java.sql.Timestamp;

public class MenuProduct {
    private Integer menu_id;
    private Integer product_id;
    private Menu menu;
    private Product product;
    private Timestamp created_at;

    public MenuProduct() {
    }

    public MenuProduct(Integer menu_id, Integer product_id, Menu menu, Product product, Timestamp created_at) {
        this.menu_id = menu_id;
        this.product_id = product_id;
        this.menu = menu;
        this.product = product;
        this.created_at = created_at;
    }

    public Integer getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(Integer menu_id) {
        this.menu_id = menu_id;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
}
