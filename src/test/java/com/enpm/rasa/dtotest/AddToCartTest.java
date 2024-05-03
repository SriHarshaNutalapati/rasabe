package com.enpm.rasa.dtotest;

import com.enpm.rasa.dto.AddToCart;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddToCartTest {

    @Test
    public void testConstructorAndGetters() {
        // Arrange
        int productId = 123;
        Long quantity = 5L;

        // Act
        AddToCart addToCart = new AddToCart(productId, quantity);

        // Assert
        assertEquals(productId, addToCart.getProductId());
        assertEquals(quantity, addToCart.getQuantity());
    }

    @Test
    public void testSetters() {
        // Arrange
        AddToCart addToCart = new AddToCart();

        // Act
        int productId = 123;
        addToCart.setProductId(productId);

        Long quantity = 5L;
        addToCart.setQuantity(quantity);

        // Assert
        assertEquals(productId, addToCart.getProductId());
        assertEquals(quantity, addToCart.getQuantity());
    }

    @Test
    public void testEqualsAndHashCode() {
        // Arrange
        AddToCart addToCart1 = new AddToCart(123, 5L);
        AddToCart addToCart2 = new AddToCart(123, 5L);
        AddToCart addToCart3 = new AddToCart(456, 5L);

        // Assert
        assertEquals(addToCart1, addToCart2);
        assertEquals(addToCart1.hashCode(), addToCart2.hashCode());
        assertEquals(addToCart1.equals(addToCart2), addToCart2.equals(addToCart1)); // Symmetric property
        assertEquals(false, addToCart1.equals(null)); // Non-nullity property
        assertEquals(false, addToCart1.equals(addToCart3)); // Equality check for different objects
    }

    @Test
    public void testToString() {
        // Arrange
        AddToCart addToCart = new AddToCart(123, 5L);

        // Act & Assert
        assertEquals("AddToCart(productId=123, quantity=5)", addToCart.toString());
    }
}

