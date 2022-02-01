package com.equalexperts.commerce;

import java.util.concurrent.atomic.AtomicInteger;

public class CartItem {
    private Product product;
    private AtomicInteger quantity;

    public CartItem(Product product) {
        this(product, 1);
    }

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = new AtomicInteger(quantity);
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity.get();
    }

    public void incrementQuantityBy(int quantity) {
        this.quantity.getAndAdd(quantity);
    }
}
