package com.equalexperts.commerce;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ShoppingCartTest {
    ShoppingCart shoppingCart;

    @Test
    @DisplayName("test creating an empty shopping cart")
    public void Should_Create_An_Empty_Shopping_Cart(){
        shoppingCart = new ShoppingCart();
        assertEquals(0, shoppingCart.getAllCartItems().size());
        assertEquals(0.0f, shoppingCart.getTotalPrice().floatValue());
        assertEquals(0.0f, shoppingCart.getTotalSalesTax().floatValue());
        assertEquals(0.0f, shoppingCart.getTotalPriceWithSalesTax().floatValue());
    }

    @Test()
    @DisplayName("test adding a product to a shopping cart with price rounded up case")
    public void Should_Add_A_Product_To_Shopping_Cart_Price_Round_Up(){
        shoppingCart = new ShoppingCart();
        shoppingCart.addCartItem(new CartItem(new Product("Olay Lotion", new BigDecimal(5.679))));

        Map<String, CartItem> cartItems = shoppingCart.getAllCartItems();
        assertEquals(1, cartItems.size());
        assertEquals(cartItems.get("Olay Lotion").getProduct().getName(), "Olay Lotion");
        assertEquals(cartItems.get("Olay Lotion").getProduct().getPrice().floatValue(), 5.679f);
        assertEquals(5.68f, shoppingCart.getTotalPrice().floatValue());
    }

    @Test
    @DisplayName("test adding a product to a shopping cart with price rounded case")
    public void Should_Add_A_Product_To_Shopping_Cart_Price_Round(){
        shoppingCart = new ShoppingCart();
        shoppingCart.addCartItem(new CartItem(new Product("iHerb Lotion", new BigDecimal(4.674))));

        Map<String, CartItem> currentCartItems = shoppingCart.getAllCartItems();
        assertEquals(1, currentCartItems.size());
        assertEquals(currentCartItems.get("iHerb Lotion").getProduct().getName(), "iHerb Lotion");
        assertEquals(currentCartItems.get("iHerb Lotion").getProduct().getPrice().floatValue(), 4.674f);
        assertEquals(4.67f, shoppingCart.getTotalPrice().floatValue());
    }

    @Test
    @DisplayName("test exception when adding null cart item")
    public void Should_Throw_Exception_When_Adding_Null_Cart_Item(){
        shoppingCart = new ShoppingCart();
        Exception e = assertThrows(IllegalArgumentException.class, () -> shoppingCart.addCartItem(null));
        assertEquals("cart item must not be null", e.getMessage());

    }

    @Test
    @DisplayName("test exception when adding null product in cart item")
    public void Should_Throw_Exception_When_Adding_Cart_Item_With_Null_Product(){
        shoppingCart = new ShoppingCart();
        Exception e = assertThrows(IllegalArgumentException.class, () -> shoppingCart.addCartItem(new CartItem(null)));
        assertEquals("product of a cart item must not be null", e.getMessage());
    }

    @Test
    @DisplayName("test exception when adding null product name in cart item")
    public void Should_Throw_Exception_When_Adding_Cart_Item_With_Null_Product_Name(){
        shoppingCart = new ShoppingCart();
        Exception e = assertThrows(IllegalArgumentException.class, () -> shoppingCart.addCartItem(new CartItem(new Product(null, new BigDecimal(3.6)))));
        assertEquals("product name must not be null", e.getMessage());
    }

    @Test
    @DisplayName("test exception when adding null product name in cart item")
    public void Should_Throw_Exception_When_Adding_Cart_Item_With_Null_Product_Price(){
        shoppingCart = new ShoppingCart();
        Exception e = assertThrows(IllegalArgumentException.class, () -> shoppingCart.addCartItem(new CartItem(new Product("Olay Lotion",null))));
        assertEquals("product price must not be null", e.getMessage());
    }


    @Test
    @DisplayName("test adding a product with quantity to shopping cart")
    public void Should_Add_A_Product_And_Quantity_To_Shopping_Cart(){
        shoppingCart = new ShoppingCart();
        shoppingCart.addCartItem(new CartItem(new Product("Dove Soap", new BigDecimal(39.99)), 5));
        shoppingCart.addCartItem(new CartItem(new Product("Dove Soap", new BigDecimal(39.99)), 3));

        Map<String, CartItem> currentCartItems = shoppingCart.getAllCartItems();
        assertEquals(1, currentCartItems.size());
        assertEquals("Dove Soap", currentCartItems.get("Dove Soap").getProduct().getName());
        assertEquals(8, currentCartItems.get("Dove Soap").getQuantity());
        assertEquals(319.92f, shoppingCart.getTotalPrice().floatValue());
    }

    @Test
    @DisplayName("test adding multiple products with quantity to shopping cart")
    public void Should_Add_Multiple_Products_And_Quantity_To_Shopping_Cart(){
        shoppingCart = new ShoppingCart();
        shoppingCart.addCartItem(new CartItem(new Product("Dove Soap", new BigDecimal(39.99)), 2));
        shoppingCart.addCartItem(new CartItem(new Product("Axe Deo", new BigDecimal(99.99)), 2));

        Map<String, CartItem> currentCartItems = shoppingCart.getAllCartItems();
        assertEquals(currentCartItems.get("Dove Soap").getProduct().getName(), "Dove Soap");
        assertEquals(currentCartItems.get("Dove Soap").getProduct().getPrice().floatValue(), 39.99f);
        assertEquals(currentCartItems.get("Dove Soap").getQuantity(), 2);

        assertEquals(currentCartItems.get("Axe Deo").getProduct().getName(), "Axe Deo");
        assertEquals(currentCartItems.get("Axe Deo").getProduct().getPrice().floatValue(), 99.99f);
        assertEquals(currentCartItems.get("Axe Deo").getQuantity(), 2);

        assertEquals(2, shoppingCart.getAllCartItems().size());
        assertEquals(4, currentCartItems.values().stream().collect(Collectors.summarizingInt(CartItem::getQuantity)).getSum());
        assertEquals(35.00f, shoppingCart.getTotalSalesTax().floatValue());
        assertEquals(314.96f, shoppingCart.getTotalPriceWithSalesTax().floatValue());
    }
}
