package com.enpm.rasa.dtotest;

import com.enpm.rasa.dto.CardDetailsRequestDTO;
import com.enpm.rasa.model.PaymentMethod;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardDetailsRequestDTOTest {

    @Test
    public void testConstructorAndGetters() {
        // Arrange
        PaymentMethod paymentMethod = PaymentMethod.CREDITCARD;
        String name = "John Doe";
        String paymentId = "12345";

        // Act
        CardDetailsRequestDTO cardDetailsRequestDTO = new CardDetailsRequestDTO(paymentMethod, name, paymentId);

        // Assert
        assertEquals(paymentMethod, cardDetailsRequestDTO.getPaymentMethod());
        assertEquals(name, cardDetailsRequestDTO.getName());
        assertEquals(paymentId, cardDetailsRequestDTO.getPaymentId());
    }

    @Test
    public void testSetters() {
        // Arrange
        CardDetailsRequestDTO cardDetailsRequestDTO = new CardDetailsRequestDTO();

        // Act
        PaymentMethod paymentMethod = PaymentMethod.CREDITCARD;
        cardDetailsRequestDTO.setPaymentMethod(paymentMethod);

        String name = "John Doe";
        cardDetailsRequestDTO.setName(name);

        String paymentId = "12345";
        cardDetailsRequestDTO.setPaymentId(paymentId);

        // Assert
        assertEquals(paymentMethod, cardDetailsRequestDTO.getPaymentMethod());
        assertEquals(name, cardDetailsRequestDTO.getName());
        assertEquals(paymentId, cardDetailsRequestDTO.getPaymentId());
    }

    @Test
    public void testEqualsAndHashCode() {
        // Arrange
        CardDetailsRequestDTO cardDetailsRequestDTO1 = new CardDetailsRequestDTO(PaymentMethod.CREDITCARD, "John Doe", "12345");
        CardDetailsRequestDTO cardDetailsRequestDTO2 = new CardDetailsRequestDTO(PaymentMethod.CREDITCARD, "John Doe", "12345");
        CardDetailsRequestDTO cardDetailsRequestDTO3 = new CardDetailsRequestDTO(PaymentMethod.PAYPAL, "Jane Doe", "54321");

        // Assert
        assertEquals(cardDetailsRequestDTO1, cardDetailsRequestDTO2);
        assertEquals(cardDetailsRequestDTO1.hashCode(), cardDetailsRequestDTO2.hashCode());
        assertEquals(cardDetailsRequestDTO1.equals(cardDetailsRequestDTO2), cardDetailsRequestDTO2.equals(cardDetailsRequestDTO1)); // Symmetric property
        assertEquals(false, cardDetailsRequestDTO1.equals(null)); // Non-nullity property
        assertEquals(false, cardDetailsRequestDTO1.equals(cardDetailsRequestDTO3)); // Equality check for different objects
    }

    @Test
    public void testToString() {
        // Arrange
        CardDetailsRequestDTO cardDetailsRequestDTO = new CardDetailsRequestDTO(PaymentMethod.CREDITCARD, "John Doe", "12345");

        // Act & Assert
        assertEquals("CardDetailsRequestDTO(paymentMethod=CREDITCARD, name=John Doe, paymentId=12345)", cardDetailsRequestDTO.toString());
    }
}

