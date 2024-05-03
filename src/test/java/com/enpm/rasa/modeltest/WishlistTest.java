package com.enpm.rasa.modeltest;

import com.enpm.rasa.model.Product;
import com.enpm.rasa.model.User;
import com.enpm.rasa.model.Wishlist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WishlistTest {

    private Wishlist wishlist;
    private User user;
    private Product product;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1);

        product = new Product();
        product.setProductId(1);
        product.setName("Test Product");

        wishlist = new Wishlist();
        wishlist.setUserId(user);
        wishlist.setProductId(product);
    }

    @Test
    void testWishlistId() {
        wishlist.setWishlistId(1);
        assertEquals(1, wishlist.getWishlistId());
    }

    @Test
    void testUserId() {
        assertEquals(user, wishlist.getUserId());
    }

    @Test
    void testProductId() {
        assertEquals(product, wishlist.getProductId());
    }

    @Test
    void testSetters() {
        User newUser = new User();
        newUser.setUserId(2);

        Product newProduct = new Product();
        newProduct.setProductId(2);
        newProduct.setName("New Product");

        wishlist.setUserId(newUser);
        wishlist.setProductId(newProduct);

        assertEquals(newUser, wishlist.getUserId());
        assertEquals(newProduct, wishlist.getProductId());
    }

    @Test
    void testBuilderPattern() {
        Wishlist newWishlist = Wishlist.builder()
                .wishlistId(1)
                .userId(user)
                .productId(product)
                .build();

        assertEquals(1, newWishlist.getWishlistId());
        assertEquals(user, newWishlist.getUserId());
        assertEquals(product, newWishlist.getProductId());
    }
}