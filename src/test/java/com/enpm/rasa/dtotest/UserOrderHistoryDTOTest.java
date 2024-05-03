package com.enpm.rasa.dtotest;

import com.enpm.rasa.dto.UserOrderHistoryDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserOrderHistoryDTOTest {

    @Test
    public void testConstructorAndGetters() {
        // Arrange
        String email = "test@example.com";

        // Act
        UserOrderHistoryDTO orderHistoryDTO = new UserOrderHistoryDTO(email);

        // Assert
        assertEquals(email, orderHistoryDTO.getEmail());
    }

    @Test
    public void testEmptyConstructor() {
        // Arrange & Act
        UserOrderHistoryDTO orderHistoryDTO = new UserOrderHistoryDTO();

        // Assert
        assertEquals(null, orderHistoryDTO.getEmail());
    }

    @Test
    public void testSetters() {
        // Arrange
        String email = "test@example.com";
        UserOrderHistoryDTO orderHistoryDTO = new UserOrderHistoryDTO();

        // Act
        orderHistoryDTO.setEmail(email);

        // Assert
        assertEquals(email, orderHistoryDTO.getEmail());
    }

    @Test
    public void testEqualsAndHashCode() {
        // Arrange
        UserOrderHistoryDTO orderHistoryDTO1 = new UserOrderHistoryDTO("test@example.com");
        UserOrderHistoryDTO orderHistoryDTO2 = new UserOrderHistoryDTO("test@example.com");
        UserOrderHistoryDTO orderHistoryDTO3 = new UserOrderHistoryDTO("another@example.com");

        // Assert
        assertEquals(orderHistoryDTO1, orderHistoryDTO2);
        assertEquals(orderHistoryDTO1.hashCode(), orderHistoryDTO2.hashCode());
        assertEquals(orderHistoryDTO1, orderHistoryDTO1); // Reflexive property
        assertEquals(orderHistoryDTO1.hashCode(), orderHistoryDTO1.hashCode()); // Consistent property
        assertEquals(orderHistoryDTO1.equals(orderHistoryDTO2), orderHistoryDTO2.equals(orderHistoryDTO1)); // Symmetric property
        assertEquals(orderHistoryDTO1.equals(orderHistoryDTO2) && orderHistoryDTO2.equals(orderHistoryDTO3), orderHistoryDTO1.equals(orderHistoryDTO3)); // Transitive property
        assertEquals(false, orderHistoryDTO1.equals(null)); // Non-nullity property
    }

    @Test
    public void testToString() {
        // Arrange
        UserOrderHistoryDTO orderHistoryDTO = new UserOrderHistoryDTO("test@example.com");

        // Act & Assert
        assertEquals("UserOrderHistoryDTO(email=test@example.com)", orderHistoryDTO.toString());
    }
}

