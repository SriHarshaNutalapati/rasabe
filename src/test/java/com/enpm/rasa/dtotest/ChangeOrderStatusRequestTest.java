package com.enpm.rasa.dtotest;

import com.enpm.rasa.dto.ChangeOrderStatusRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChangeOrderStatusRequestTest {

    @Test
    public void testConstructorAndGetters() {
        // Arrange
        String orderStatus = "Shipped";

        // Act
        ChangeOrderStatusRequest changeOrderStatusRequest = new ChangeOrderStatusRequest(orderStatus);

        // Assert
        assertEquals(orderStatus, changeOrderStatusRequest.getOrderStatus());
    }

    @Test
    public void testSetters() {
        // Arrange
        ChangeOrderStatusRequest changeOrderStatusRequest = new ChangeOrderStatusRequest();

        // Act
        String orderStatus = "Shipped";
        changeOrderStatusRequest.setOrderStatus(orderStatus);

        // Assert
        assertEquals(orderStatus, changeOrderStatusRequest.getOrderStatus());
    }

    @Test
    public void testEqualsAndHashCode() {
        // Arrange
        ChangeOrderStatusRequest request1 = new ChangeOrderStatusRequest("Shipped");
        ChangeOrderStatusRequest request2 = new ChangeOrderStatusRequest("Shipped");
        ChangeOrderStatusRequest request3 = new ChangeOrderStatusRequest("Delivered");

        // Assert
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertEquals(request1.equals(request2), request2.equals(request1)); // Symmetric property
        assertEquals(false, request1.equals(null)); // Non-nullity property
        assertEquals(false, request1.equals(request3)); // Equality check for different objects
    }

    @Test
    public void testToString() {
        // Arrange
        ChangeOrderStatusRequest request = new ChangeOrderStatusRequest("Shipped");

        // Act & Assert
        assertEquals("ChangeOrderStatusRequest(orderStatus=Shipped)", request.toString());
    }
}

