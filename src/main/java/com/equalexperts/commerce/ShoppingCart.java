package com.equalexperts.commerce;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;


public class ShoppingCart {
    private Map<String, CartItem> cartItems;

    public ShoppingCart(){
        this.cartItems = new HashMap<>();
    }

    public void addCartItem(CartItem cartItem){
        validate(cartItem);
        String productName = cartItem.getProduct().getName();
        if(cartItems.containsKey(productName)) {
            incrementQuantityInternal(cartItem);
        } else {
            synchronized (this) {
                if(!cartItems.containsKey(productName)){
                    addItemInternal(cartItem);
                } else {
                    incrementQuantityInternal(cartItem);
                }
            }
        }
    }

    public BigDecimal getTotalPrice(){
        BigDecimal totalPrice = new BigDecimal(0);
        for(CartItem cartItem : cartItems.values()){
            BigDecimal unitPrice = cartItem.getProduct().getPrice();
            totalPrice = totalPrice.add(unitPrice.multiply(new BigDecimal(cartItem.getQuantity())));
        }
        return roundUp(totalPrice);
    }

    public BigDecimal getTotalSalesTax(){
        BigDecimal salesTaxRate = new BigDecimal(0.125);
        BigDecimal totalSalesTax = getTotalPrice().multiply(salesTaxRate);
        return roundUp(totalSalesTax);
    }

    public BigDecimal getTotalPriceWithSalesTax(){
        BigDecimal totalPriceWithSalesTax = getTotalPrice().add(getTotalSalesTax());
        return roundUp(totalPriceWithSalesTax);
    }

    public Map<String, CartItem> getAllCartItems() {
        return Collections.unmodifiableMap(cartItems);
    }

    private void validate(CartItem cartItem) throws IllegalArgumentException {
        if(cartItem == null) throw new IllegalArgumentException("cart item must not be null");
        if(cartItem.getProduct() == null) throw new IllegalArgumentException("product of a cart item must not be null");
        if(StringUtils.isBlank(cartItem.getProduct().getName())) throw new IllegalArgumentException("product name must not be null");
        if(cartItem.getProduct().getPrice() == null) throw new IllegalArgumentException("product price must not be null");
    }

    private void addItemInternal(CartItem cartItem) {
        this.cartItems.put(cartItem.getProduct().getName(), cartItem);
    }

    private void incrementQuantityInternal(CartItem cartItem){
        this.cartItems.get(cartItem.getProduct().getName()).incrementQuantityBy(cartItem.getQuantity());
    }

    private BigDecimal roundUp(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }
}
