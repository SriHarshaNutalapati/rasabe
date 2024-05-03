package com.enpm.rasa.modeltest;

import com.enpm.rasa.model.Cart;
import com.enpm.rasa.model.Product;
import com.enpm.rasa.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CartTest {

    private Cart cart;
    private User user;
    private Product product;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1);

        product = new Product();
        product.setProductId(1);
        product.setName("Test Product");

        cart = new Cart();
        cart.setUserId(user);
        cart.setProductId(product);
        cart.setQuantity(2L);
        cart.setAmount(100L);
    }

    @Test
    void testCartId() {
        cart.setCartId(1);
        assertEquals(1, cart.getCartId());
    }

    @Test
    void testUserId() {
        assertEquals(user, cart.getUserId());
    }

    @Test
    void testProductId() {
        assertEquals(product, cart.getProductId());
    }

    @Test
    void testQuantity() {
        assertEquals(2L, cart.getQuantity());
    }

    @Test
    void testAmount() {
        assertEquals(100L, cart.getAmount());
    }

    @Test
    void testSetters() {
        User newUser = new User();
        newUser.setUserId(2);

        Product newProduct = new Product();
        newProduct.setProductId(2);
        newProduct.setName("New Product");

        cart.setUserId(newUser);
        cart.setProductId(newProduct);
        cart.setQuantity(3L);
        cart.setAmount(200L);

        assertEquals(newUser, cart.getUserId());
        assertEquals(newProduct, cart.getProductId());
        assertEquals(3L, cart.getQuantity());
        assertEquals(200L, cart.getAmount());
    }

    @Test
    void testBuilderPattern() {
        Cart newCart = Cart.builder()
                .cartId(1)
                .userId(user)
                .productId(product)
                .quantity(2L)
                .amount(100L)
                .build();

        assertEquals(1, newCart.getCartId());
        assertEquals(user, newCart.getUserId());
        assertEquals(product, newCart.getProductId());
        assertEquals(2L, newCart.getQuantity());
        assertEquals(100L, newCart.getAmount());
    }
}