package com.ecommerce.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.demo.model.dto.LoginDTO;
import com.ecommerce.demo.service.AuthService;


@RestController
@CrossOrigin(origins={"*"})
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDto) throws Exception {
        return ResponseEntity.ok(this.authService.login(loginDto));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> postMethodName(@RequestBody String refreshToken) throws Exception {
        return ResponseEntity.ok(this.authService.getRefreshToken(refreshToken));
    }
    
    
    
}
