package com.example.dogfoodapp;

public class Product {
    private final String productId;
    private final String name;
    private final String category;
    private final double price;
    private final int quantity;

    public Product(String productId, String name, String category, double price, int quantity) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    public String getProductId() { return productId; }
    public String getName() { return name; }
    public String getCategory() { return category; } // Optional
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
}