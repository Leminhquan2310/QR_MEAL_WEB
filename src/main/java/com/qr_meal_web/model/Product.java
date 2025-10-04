package com.qr_meal_web.model;

import com.qr_meal_web.enums.ProductStatus;

import java.sql.Timestamp;

public class Product {
    private int id;
    private String name;
    private String description;
    private double price;
    private ProductStatus status;
    private Category category;
    private String image;
    private int cooking_time;
    private Timestamp created_at;

    public Product() {
    }

    public Product(int id, String name, String description, double price, ProductStatus status, Category category, String image, int cooking_time) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.status = status;
        this.category = category;
        this.image = image;
        this.cooking_time = cooking_time;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCooking_time() {
        return cooking_time;
    }

    public void setCooking_time(int cooking_time) {
        this.cooking_time = cooking_time;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", status=" + status +
                ", category=" + category +
                ", image='" + image + '\'' +
                ", cooking_time=" + cooking_time +
                ", created_at=" + created_at +
                '}';
    }
}
