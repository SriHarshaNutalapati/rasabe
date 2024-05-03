package com.enpm.rasa.dtotest;

import com.enpm.rasa.dto.AddProduct;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddProductTest {

    @Test
    public void testConstructorAndGetters() {
        // Arrange
        String name = "Product";
        String description = "Description";
        Long price = 100L;
        String tagName = "Tag";
        Long quantity = 10L;
        String imageUrl = "image.jpg";

        // Act
        AddProduct addProduct = new AddProduct(name, description, price, tagName, quantity, imageUrl);

        // Assert
        assertEquals(name, addProduct.getName());
        assertEquals(description, addProduct.getDescription());
        assertEquals(price, addProduct.getPrice());
        assertEquals(tagName, addProduct.getTagName());
        assertEquals(quantity, addProduct.getQuantity());
        assertEquals(imageUrl, addProduct.getImageUrl());
    }

    @Test
    public void testSetters() {
        // Arrange
        AddProduct addProduct = new AddProduct();

        // Act
        String name = "Product";
        addProduct.setName(name);

        String description = "Description";
        addProduct.setDescription(description);

        Long price = 100L;
        addProduct.setPrice(price);

        String tagName = "Tag";
        addProduct.setTagName(tagName);

        Long quantity = 10L;
        addProduct.setQuantity(quantity);

        String imageUrl = "image.jpg";
        addProduct.setImageUrl(imageUrl);

        // Assert
        assertEquals(name, addProduct.getName());
        assertEquals(description, addProduct.getDescription());
        assertEquals(price, addProduct.getPrice());
        assertEquals(tagName, addProduct.getTagName());
        assertEquals(quantity, addProduct.getQuantity());
        assertEquals(imageUrl, addProduct.getImageUrl());
    }

    @Test
    public void testEqualsAndHashCode() {
        // Arrange
        AddProduct addProduct1 = new AddProduct("Product", "Description", 100L, "Tag", 10L, "image.jpg");
        AddProduct addProduct2 = new AddProduct("Product", "Description", 100L, "Tag", 10L, "image.jpg");
        AddProduct addProduct3 = new AddProduct("AnotherProduct", "Description", 100L, "Tag", 10L, "image.jpg");

        // Assert
        assertEquals(addProduct1, addProduct2);
        assertEquals(addProduct1.hashCode(), addProduct2.hashCode());
        assertEquals(addProduct1.equals(addProduct2), addProduct2.equals(addProduct1)); // Symmetric property
        assertEquals(false, addProduct1.equals(null)); // Non-nullity property
        assertEquals(false, addProduct1.equals(addProduct3)); // Equality check for different objects
    }

    @Test
    public void testToString() {
        // Arrange
        AddProduct addProduct = new AddProduct("Product", "Description", 100L, "Tag", 10L, "image.jpg");

        // Act & Assert
        assertEquals("AddProduct(name=Product, description=Description, price=100, tagName=Tag, quantity=10, imageUrl=image.jpg)", addProduct.toString());
    }
}

