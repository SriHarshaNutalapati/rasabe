package com.enpm.rasa.servicetest;

import com.enpm.rasa.configuration.JwtService;
import com.enpm.rasa.dto.AuthenticationRequest;
import com.enpm.rasa.dto.RegisterRequest;
import com.enpm.rasa.model.Role;
import com.enpm.rasa.model.User;
import com.enpm.rasa.repository.UserRepository;
import com.enpm.rasa.services.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthenticationServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationService authenticationService;

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
        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .password(passwordEncoder.encode("password"))
                .role(Role.CUSTOMER)
                .build();

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        // Act
        ResponseEntity<Object> response = authenticationService.register(request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testLogin() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest("john@example.com", "password");
        User user = User.builder()
                .userId(1)
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .password("encodedPassword")
                .role(Role.CUSTOMER)
                .build();
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("token");

        // Act
        ResponseEntity<Object> response = authenticationService.login(request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
    }
}