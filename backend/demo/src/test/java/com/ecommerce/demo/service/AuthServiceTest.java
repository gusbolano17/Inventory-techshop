package com.ecommerce.demo.service;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ecommerce.demo.model.Role;
import com.ecommerce.demo.model.Users;
import com.ecommerce.demo.model.dto.LoginDTO;
import com.ecommerce.demo.model.enums.TokenType;
import com.ecommerce.demo.utils.JwtService;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private Users testUser;
    private LoginDTO loginDTO;

    @BeforeEach
    void setUp() {
        testUser = new Users();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        
        Role role = new Role();
        role.setName("ROLE_USER");
        testUser.setRole(role);
        
        loginDTO = new LoginDTO("test@example.com", "password");
    }

    @Test
    void login_shouldReturnTokensOnSuccess() throws Exception {
        Authentication mockAuth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockAuth);
        when(usuarioService.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(jwtService.generateToken(testUser, TokenType.ACCESS_TOKEN)).thenReturn("access-token");
        when(jwtService.generateToken(testUser, TokenType.REFRESH_TOKEN)).thenReturn("refresh-token");
        doAnswer(invocation -> null).when(usuarioService).registrarRefreshToken(any(), any());

        Map<String, Object> result = authService.login(loginDTO);

        assertNotNull(result);
        assertEquals("access-token", result.get("accessToken"));
        assertEquals("refresh-token", result.get("refreshToken"));
    }

    @Test
    void login_shouldThrowExceptionWhenUserNotFound() throws Exception {
        Authentication mockAuth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockAuth);
        when(usuarioService.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> authService.login(loginDTO));
    }

    @Test
    void getRefreshToken_shouldThrowExceptionOnInvalidToken() throws Exception {
        when(jwtService.isValidRefreshToken("invalid-token")).thenReturn(false);

        Exception exception = assertThrows(Exception.class, () -> authService.getRefreshToken("invalid-token"));
        assertEquals("El token proporcionado no es valido", exception.getMessage());
    }
}
