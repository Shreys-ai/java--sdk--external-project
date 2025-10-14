package com.automation.utils;

public class Product {
    private final String name;
    private final String price;
    private final String category;
    private final String stock;
    private final String description;

    public Product(String name, String price, String category, String stock, String description) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.stock = stock;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getStock() {
        return stock;
    }

    public String getDescription() {
        return description;
    }
}