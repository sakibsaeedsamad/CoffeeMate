package com.example.coffeemate;

public class Order {
    private String userId;
    private String productId;

    public Order(String userId, String productId) {
        this.userId = userId;
        this.productId = productId;
    }

    public String getUserId() {
        return userId;
    }

    public String getProductId() {
        return productId;
    }
}
