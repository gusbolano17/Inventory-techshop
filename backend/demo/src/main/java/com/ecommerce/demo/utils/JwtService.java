package com.ecommerce.demo.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ecommerce.demo.model.enums.TokenType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private Long expirationTime;

    @Value("${security.jwt.refresh-key}")
    private String secretRefresh;

    @Value("${security.jwt.expiration-refresh-time}")
    private Long expirationRefresh;

    public String generateToken(UserDetails details, TokenType tokenType) {

        Long expTime = (tokenType == TokenType.ACCESS_TOKEN)
                ? expirationTime
                : expirationRefresh;

        var secret = (tokenType == TokenType.ACCESS_TOKEN)
                ? secretKey.getBytes()
                : secretRefresh.getBytes();

        Map<String, Object> claims = new HashMap<>();

        if (tokenType == TokenType.ACCESS_TOKEN) {
            String role = details.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse(null);
            claims.put("role", role);
        }

        return Jwts.builder()
                .subject(details.getUsername())
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expTime))
                .signWith(Keys.hmacShaKeyFor(secret))
                .compact();
    }

    public String extractAccessEmail(String token) {
        return extractEmail(token, secretKey);
    }

    public String extractRefreshEmail(String token) {
        return extractEmail(token, secretRefresh);
    }

    public String extractRole(String token) {
        return extractClaims(token, secretKey).get("role", String.class);
    }

    public boolean isValidAccessToken(String token) {
        return isTokenValid(token, secretKey);
    }

    public boolean isValidRefreshToken(String token) {
        return isTokenValid(token, secretRefresh);
    }

    private String extractEmail(String token, String secret) {
        return extractClaims(token, secret).getSubject();
    }

    private boolean isTokenValid(String token, String secret) {
        try {
            extractClaims(token, secret);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims extractClaims(String token, String secret) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
