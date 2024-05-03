package com.enpm.rasa.modeltest;

import com.enpm.rasa.model.Order;
import com.enpm.rasa.model.OrderItem;
import com.enpm.rasa.model.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    @Test
    void testOrderItemBuilder() {
        Order order = new Order();
        Product product = new Product();
        OrderItem orderItem = OrderItem.builder()
                .orderId(order)
                .productId(product)
                .quantity(2L)
                .unitPrice(50L)
                .build();

        assertNotNull(orderItem);
        assertEquals(order, orderItem.getOrderId());
        assertEquals(product, orderItem.getProductId());
        assertEquals(2L, orderItem.getQuantity());
        assertEquals(50L, orderItem.getUnitPrice());
    }
}