package com.ecommerce.demo.utils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import com.ecommerce.demo.model.enums.TokenType;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    private static final String SECRET_KEY = "this-is-a-very-long-secret-key-for-testing-purposes-at-least-32-bytes";
    private static final String SECRET_REFRESH = "this-is-a-very-long-refresh-key-for-testing-purposes-32-bytes";
    private static final Long EXPIRATION_TIME = 3600000L;
    private static final Long EXPIRATION_REFRESH_TIME = 86400000L;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtService, "secretRefresh", SECRET_REFRESH);
        ReflectionTestUtils.setField(jwtService, "expirationTime", EXPIRATION_TIME);
        ReflectionTestUtils.setField(jwtService, "expirationRefresh", EXPIRATION_REFRESH_TIME);
        
        when(userDetails.getUsername()).thenReturn("test@example.com");
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());
    }

    @Test
    void generateToken_shouldGenerateAccessToken() {
        String token = jwtService.generateToken(userDetails, TokenType.ACCESS_TOKEN);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void generateToken_shouldGenerateRefreshToken() {
        String token = jwtService.generateToken(userDetails, TokenType.REFRESH_TOKEN);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractAccessEmail_shouldExtractEmailFromAccessToken() {
        String token = jwtService.generateToken(userDetails, TokenType.ACCESS_TOKEN);
        String email = jwtService.extractAccessEmail(token);

        assertEquals("test@example.com", email);
    }

    @Test
    void extractRefreshEmail_shouldExtractEmailFromRefreshToken() {
        String token = jwtService.generateToken(userDetails, TokenType.REFRESH_TOKEN);
        String email = jwtService.extractRefreshEmail(token);

        assertEquals("test@example.com", email);
    }

    @Test
    void isValidAccessToken_shouldReturnTrueForValidToken() {
        String token = jwtService.generateToken(userDetails, TokenType.ACCESS_TOKEN);

        assertTrue(jwtService.isValidAccessToken(token));
    }

    @Test
    void isValidAccessToken_shouldReturnFalseForInvalidToken() {
        assertFalse(jwtService.isValidAccessToken("invalid.token.here"));
    }

    @Test
    void isValidRefreshToken_shouldReturnTrueForValidToken() {
        String token = jwtService.generateToken(userDetails, TokenType.REFRESH_TOKEN);

        assertTrue(jwtService.isValidRefreshToken(token));
    }

    @Test
    void isValidRefreshToken_shouldReturnFalseForInvalidToken() {
        assertFalse(jwtService.isValidRefreshToken("invalid.refresh.token"));
    }

    @Test
    void extractRole_shouldReturnNullWhenNoRole() {
        String token = jwtService.generateToken(userDetails, TokenType.ACCESS_TOKEN);
        String role = jwtService.extractRole(token);

        assertNull(role);
    }
}
