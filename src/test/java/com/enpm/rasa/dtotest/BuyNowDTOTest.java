package com.enpm.rasa.dtotest;

import com.enpm.rasa.dto.BuyNowDTO;
import com.enpm.rasa.model.PaymentMethod;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BuyNowDTOTest {

    @Test
    public void testConstructorAndGetters() {
        // Arrange
        PaymentMethod paymentMethod = PaymentMethod.CREDITCARD;

        // Act
        BuyNowDTO buyNowDTO = new BuyNowDTO(paymentMethod);

        // Assert
        assertEquals(paymentMethod, buyNowDTO.getPaymentMethod());
    }

    @Test
    public void testSetters() {
        // Arrange
        BuyNowDTO buyNowDTO = new BuyNowDTO();

        // Act
        PaymentMethod paymentMethod = PaymentMethod.CREDITCARD;
        buyNowDTO.setPaymentMethod(paymentMethod);

        // Assert
        assertEquals(paymentMethod, buyNowDTO.getPaymentMethod());
    }

    @Test
    public void testEqualsAndHashCode() {
        // Arrange
        BuyNowDTO buyNowDTO1 = new BuyNowDTO(PaymentMethod.CREDITCARD);
        BuyNowDTO buyNowDTO2 = new BuyNowDTO(PaymentMethod.CREDITCARD);
        BuyNowDTO buyNowDTO3 = new BuyNowDTO(PaymentMethod.PAYPAL);

        // Assert
        assertEquals(buyNowDTO1, buyNowDTO2);
        assertEquals(buyNowDTO1.hashCode(), buyNowDTO2.hashCode());
        assertEquals(buyNowDTO1.equals(buyNowDTO2), buyNowDTO2.equals(buyNowDTO1)); // Symmetric property
        assertEquals(false, buyNowDTO1.equals(null)); // Non-nullity property
        assertEquals(false, buyNowDTO1.equals(buyNowDTO3)); // Equality check for different objects
    }

    @Test
    public void testToString() {
        // Arrange
        BuyNowDTO buyNowDTO = new BuyNowDTO(PaymentMethod.CREDITCARD);

        // Act & Assert
        assertEquals("BuyNowDTO(paymentMethod=CREDITCARD)", buyNowDTO.toString());
    }
}

