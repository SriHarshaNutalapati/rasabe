package com.enpm.rasa.modeltest;

import com.enpm.rasa.model.Inventory;
import com.enpm.rasa.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InventoryTest {

    private Inventory inventory;
    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId(1);
        product.setName("Test Product");

        inventory = new Inventory();
        inventory.setQuantityLeft(100L);
        inventory.setProduct(product);
    }

    @Test
    void testInventoryId() {
        inventory.setInventoryId(1);
        assertEquals(1, inventory.getInventoryId());
    }

    @Test
    void testQuantityLeft() {
        assertEquals(100L, inventory.getQuantityLeft());
    }

    @Test
    void testProduct() {
        assertEquals(product, inventory.getProduct());
    }

    @Test
    void testSetters() {
        Product newProduct = new Product();
        newProduct.setProductId(2);
        newProduct.setName("New Product");

        inventory.setQuantityLeft(200L);
        inventory.setProduct(newProduct);

        assertEquals(200L, inventory.getQuantityLeft());
        assertEquals(newProduct, inventory.getProduct());
    }

    @Test
    void testBuilderPattern() {
        Inventory newInventory = Inventory.builder()
                .inventoryId(1)
                .quantityLeft(100L)
                .product(product)
                .build();

        assertEquals(1, newInventory.getInventoryId());
        assertEquals(100L, newInventory.getQuantityLeft());
        assertEquals(product, newInventory.getProduct());
    }
}