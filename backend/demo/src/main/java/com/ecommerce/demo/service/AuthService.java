package com.ecommerce.demo.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.ecommerce.demo.model.Users;
import com.ecommerce.demo.model.dto.LoginDTO;
import com.ecommerce.demo.model.enums.TokenType;
import com.ecommerce.demo.utils.JwtService;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private JwtService jwtService;

    public Map<String, Object> login(LoginDTO body) throws Exception {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(body.email(), body.password()));

        Users usuario = usuarioService.findByEmail(body.email()).orElseThrow(
                () -> new UsernameNotFoundException("Usuario no encontrado"));

        String accessToken = jwtService.generateToken(usuario, TokenType.ACCESS_TOKEN);
        String refreshToken = jwtService.generateToken(usuario, TokenType.REFRESH_TOKEN);

        String rtHashed = hashearToken(refreshToken);
        this.usuarioService.registrarRefreshToken(usuario, rtHashed);

        return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
    }

    public Map<String, Object> getRefreshToken(String token) throws Exception {

        if (!jwtService.isValidRefreshToken(token)) {
            throw new Exception("El token proporcionado no es valido");
        }

        String email = jwtService.extractRefreshEmail(token);

        Users usuario = usuarioService.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("Usuario no encontrado"));

        if (!BCrypt.checkpw(token, usuario.getRefreshToken())) {
            throw new Exception("Este token proporcionado no coincide con el registrado");
        }

        String accessToken = this.jwtService.generateToken(usuario, TokenType.ACCESS_TOKEN);
        String refreshToken = this.jwtService.generateToken(usuario, TokenType.REFRESH_TOKEN);

        String rtHashed = hashearToken(refreshToken);

        this.usuarioService.registrarRefreshToken(usuario, rtHashed);

        return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
    }

    private String hashearToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

}
