package com.enpm.rasa.dtotest;

import com.enpm.rasa.dto.RegisterRequest;
import com.enpm.rasa.model.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegisterRequestTest {

    @Test
    public void testConstructor() {
        String email = "john@example.com";
        String password = "password";
        String firstName = "John";
        String lastName = "Doe";
        Role role = Role.CUSTOMER;

        RegisterRequest registerRequest = new RegisterRequest(email, password, firstName, lastName, role);

        assertEquals(email, registerRequest.getEmail());
        assertEquals(password, registerRequest.getPassword());
        assertEquals(firstName, registerRequest.getFirstName());
        assertEquals(lastName, registerRequest.getLastName());
        assertEquals(role, registerRequest.getRole());
    }

    @Test
    public void testBuilder() {
        String email = "john@example.com";
        String password = "password";
        String firstName = "John";
        String lastName = "Doe";
        Role role = Role.CUSTOMER;

        RegisterRequest registerRequest = RegisterRequest.builder()
                .email(email)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .role(role)
                .build();

        assertEquals(email, registerRequest.getEmail());
        assertEquals(password, registerRequest.getPassword());
        assertEquals(firstName, registerRequest.getFirstName());
        assertEquals(lastName, registerRequest.getLastName());
        assertEquals(role, registerRequest.getRole());
    }

    @Test
    public void testSetters() {
        RegisterRequest registerRequest = new RegisterRequest();

        String email = "john@example.com";
        String password = "password";
        String firstName = "John";
        String lastName = "Doe";
        Role role = Role.CUSTOMER;

        registerRequest.setEmail(email);
        registerRequest.setPassword(password);
        registerRequest.setFirstName(firstName);
        registerRequest.setLastName(lastName);
        registerRequest.setRole(role);

        assertEquals(email, registerRequest.getEmail());
        assertEquals(password, registerRequest.getPassword());
        assertEquals(firstName, registerRequest.getFirstName());
        assertEquals(lastName, registerRequest.getLastName());
        assertEquals(role, registerRequest.getRole());
    }
}