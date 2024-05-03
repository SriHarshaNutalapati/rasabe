package com.enpm.rasa.configurationtest;

import com.enpm.rasa.configuration.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtServiceTest {

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private JwtService jwtService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(userDetails.getUsername()).thenReturn("testUser");
    }

    @Test
    public void testExtractUsername() {
        // Arrange
        String token = jwtService.generateToken(userDetails);

        // Act
        String username = jwtService.extractUsername(token);

        // Assert
        assertEquals("testUser", username);
    }

    @Test
    public void testExtractClaim() {
        // Arrange
        String token = jwtService.generateToken(userDetails);

        // Act
        String username = jwtService.extractClaim(token, Claims::getSubject);

        // Assert
        assertEquals("testUser", username);
    }

    @Test
    public void testGenerateToken() {
        // Arrange
        String token = jwtService.generateToken(userDetails);

        // Assert
        assertNotNull(token);
    }

    @Test
    public void testGenerateTokenWithExtraClaims() {
        // Arrange
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("key", "value");

        // Act
        String token = jwtService.generateToken(extraClaims, userDetails);

        // Assert
        assertNotNull(token);
    }

    @Test
    public void testIsTokenValid() {
        // Arrange
        String token = jwtService.generateToken(userDetails);

        // Act
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // Assert
        assertTrue(isValid);
    }

    @Test
    public void testIsTokenInvalidDueToExpiration() {
        // Arrange
        String token = jwtService.generateToken(userDetails);
        JwtService jwtServiceSpy = spy(jwtService);
        when(jwtServiceSpy.extractExpiration(token)).thenReturn(new Date(System.currentTimeMillis() - 1000)); // Set expiration in past

        // Act
        boolean isValid = jwtServiceSpy.isTokenValid(token, userDetails);

        // Assert
        assertFalse(isValid);
    }

    @Test
    public void testIsTokenInvalidDueToMismatchedUsername() {
        // Arrange
        String token = jwtService.generateToken(userDetails);
        when(userDetails.getUsername()).thenReturn("differentUser");

        // Act
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // Assert
        assertFalse(isValid);
    }

    @Test
    public void testExtractClaimWithMalformedToken() {
        // Arrange
        String invalidToken = "invalidToken";

        // Assert
        assertThrows(MalformedJwtException.class, () -> jwtService.extractClaim(invalidToken, Claims::getSubject));
    }

    @Test
    public void testExtractExpiration() {
        // Arrange
        String token = jwtService.generateToken(userDetails);

        // Act
        Date expiration = jwtService.extractExpiration(token);

        // Assert
        assertNotNull(expiration);
    }
}

