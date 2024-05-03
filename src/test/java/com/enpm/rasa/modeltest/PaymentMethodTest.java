package com.enpm.rasa.modeltest;

import com.enpm.rasa.model.PaymentMethod;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentMethodTest {

    @Test
    void testPaymentMethodValues() {
        PaymentMethod[] paymentMethods = PaymentMethod.values();
        assertEquals(3, paymentMethods.length);
        assertArrayEquals(new PaymentMethod[]{PaymentMethod.CREDITCARD, PaymentMethod.CASH, PaymentMethod.PAYPAL}, paymentMethods);
    }
}