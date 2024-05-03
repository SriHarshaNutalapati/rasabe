package com.enpm.rasa.dtotest;

import com.enpm.rasa.dto.AuthenticationResponse;
import com.enpm.rasa.model.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthenticationResponseTest {

    @Test
    public void testConstructorAndGetters() {
        // Arrange
        String token = "token";
        int id = 1;
        String name = "name";
        Role role = Role.ADMIN;
        String email = "test@example.com";

        // Act
        AuthenticationResponse authResponse = new AuthenticationResponse(token, id, name, role, email);

        // Assert
        assertEquals(token, authResponse.getToken());
        assertEquals(id, authResponse.getId());
        assertEquals(name, authResponse.getName());
        assertEquals(role, authResponse.getRole());
        assertEquals(email, authResponse.getEmail());
    }

    @Test
    public void testSetters() {
        // Arrange
        AuthenticationResponse authResponse = new AuthenticationResponse();

        // Act
        String token = "token";
        authResponse.setToken(token);

        int id = 1;
        authResponse.setId(id);

        String name = "name";
        authResponse.setName(name);

        Role role = Role.ADMIN;
        authResponse.setRole(role);

        String email = "test@example.com";
        authResponse.setEmail(email);

        // Assert
        assertEquals(token, authResponse.getToken());
        assertEquals(id, authResponse.getId());
        assertEquals(name, authResponse.getName());
        assertEquals(role, authResponse.getRole());
        assertEquals(email, authResponse.getEmail());
    }

    @Test
    public void testEqualsAndHashCode() {
        // Arrange
        AuthenticationResponse authResponse1 = new AuthenticationResponse("token", 1, "name", Role.ADMIN, "test@example.com");
        AuthenticationResponse authResponse2 = new AuthenticationResponse("token", 1, "name", Role.ADMIN, "test@example.com");
        AuthenticationResponse authResponse3 = new AuthenticationResponse("anotherToken", 2, "anotherName", Role.CUSTOMER, "another@example.com");

        // Assert
        assertEquals(authResponse1, authResponse2);
        assertEquals(authResponse1.hashCode(), authResponse2.hashCode());
        assertEquals(authResponse1.equals(authResponse2), authResponse2.equals(authResponse1)); // Symmetric property
        assertEquals(false, authResponse1.equals(null)); // Non-nullity property
        assertEquals(false, authResponse1.equals(authResponse3)); // Equality check for different objects
    }

    @Test
    public void testToString() {
        // Arrange
        AuthenticationResponse authResponse = new AuthenticationResponse("token", 1, "name", Role.ADMIN, "test@example.com");

        // Act & Assert
        assertEquals("AuthenticationResponse(token=token, id=1, name=name, role=ADMIN, email=test@example.com)", authResponse.toString());
    }
}

