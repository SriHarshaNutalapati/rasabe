package com.enpm.rasa.dtotest;

import com.enpm.rasa.dto.AuthenticationRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthenticationRequestTest {

    @Test
    public void testConstructorAndGetters() {
        // Arrange
        String email = "test@example.com";
        String password = "password";

        // Act
        AuthenticationRequest authRequest = new AuthenticationRequest(email, password);

        // Assert
        assertEquals(email, authRequest.getEmail());
        assertEquals(password, authRequest.getPassword());
    }

    @Test
    public void testSetters() {
        // Arrange
        AuthenticationRequest authRequest = new AuthenticationRequest();

        // Act
        String email = "test@example.com";
        authRequest.setEmail(email);

        String password = "password";
        authRequest.setPassword(password);

        // Assert
        assertEquals(email, authRequest.getEmail());
        assertEquals(password, authRequest.getPassword());
    }

    @Test
    public void testEqualsAndHashCode() {
        // Arrange
        AuthenticationRequest authRequest1 = new AuthenticationRequest("test@example.com", "password");
        AuthenticationRequest authRequest2 = new AuthenticationRequest("test@example.com", "password");
        AuthenticationRequest authRequest3 = new AuthenticationRequest("another@example.com", "password");

        // Assert
        assertEquals(authRequest1, authRequest2);
        assertEquals(authRequest1.hashCode(), authRequest2.hashCode());
        assertEquals(authRequest1.equals(authRequest2), authRequest2.equals(authRequest1)); // Symmetric property
        assertEquals(false, authRequest1.equals(null)); // Non-nullity property
        assertEquals(false, authRequest1.equals(authRequest3)); // Equality check for different objects
    }

    @Test
    public void testToString() {
        // Arrange
        AuthenticationRequest authRequest = new AuthenticationRequest("test@example.com", "password");

        // Act & Assert
        assertEquals("AuthenticationRequest(email=test@example.com, password=password)", authRequest.toString());
    }
}
