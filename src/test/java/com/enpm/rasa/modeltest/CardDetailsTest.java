package com.enpm.rasa.modeltest;

import com.enpm.rasa.model.CardDetails;
import com.enpm.rasa.model.PaymentMethod;
import com.enpm.rasa.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CardDetailsTest {

    private CardDetails cardDetails;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1);

        cardDetails = new CardDetails();
        cardDetails.setUserId(user);
        cardDetails.setPaymentMethod(PaymentMethod.CREDITCARD);
        cardDetails.setName("Test Card");
        cardDetails.setPaymentId("123456789");
    }

    @Test
    void testCardId() {
        cardDetails.setCardId(1);
        assertEquals(1, cardDetails.getCardId());
    }

    @Test
    void testUserId() {
        assertEquals(user, cardDetails.getUserId());
    }

    @Test
    void testPaymentMethod() {
        assertEquals(PaymentMethod.CREDITCARD, cardDetails.getPaymentMethod());
    }

    @Test
    void testName() {
        assertEquals("Test Card", cardDetails.getName());
    }

    @Test
    void testPaymentId() {
        assertEquals("123456789", cardDetails.getPaymentId());
    }

    @Test
    void testSetters() {
        User newUser = new User();
        newUser.setUserId(2);

        cardDetails.setUserId(newUser);
        cardDetails.setPaymentMethod(PaymentMethod.PAYPAL);
        cardDetails.setName("New Card");
        cardDetails.setPaymentId("987654321");

        assertEquals(newUser, cardDetails.getUserId());
        assertEquals(PaymentMethod.PAYPAL, cardDetails.getPaymentMethod());
        assertEquals("New Card", cardDetails.getName());
        assertEquals("987654321", cardDetails.getPaymentId());
    }

    @Test
    void testBuilderPattern() {
        CardDetails newCardDetails = CardDetails.builder()
                .cardId(1)
                .userId(user)
                .paymentMethod(PaymentMethod.CREDITCARD)
                .name("Test Card")
                .paymentId("123456789")
                .build();

        assertEquals(1, newCardDetails.getCardId());
        assertEquals(user, newCardDetails.getUserId());
        assertEquals(PaymentMethod.CREDITCARD, newCardDetails.getPaymentMethod());
        assertEquals("Test Card", newCardDetails.getName());
        assertEquals("123456789", newCardDetails.getPaymentId());
    }
}