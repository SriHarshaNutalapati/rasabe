package com.enpm.rasa.controllertest;

import com.enpm.rasa.controller.AuthenticationController;
import com.enpm.rasa.dto.AuthenticationRequest;
import com.enpm.rasa.dto.RegisterRequest;
import com.enpm.rasa.model.Role;
import com.enpm.rasa.services.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    public void testRegister() {
        // Arrange
        RegisterRequest request = RegisterRequest.builder()
                .email("john@example.com")
                .password("password")
                .firstName("John")
                .lastName("Doe")
                .role(Role.CUSTOMER) // Provide the role if required
                .build();
        when(authenticationService.register(request)).thenReturn(ResponseEntity.ok().build());

        // Act
        ResponseEntity<Object> response = authenticationController.register(request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testLogin() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest("john@example.com", "password");
        when(authenticationService.login(request)).thenReturn(ResponseEntity.ok().build());

        // Act
        ResponseEntity<Object> response = authenticationController.login(request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
    }
}