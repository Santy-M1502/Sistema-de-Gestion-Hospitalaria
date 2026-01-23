package com.SGH.hospital.controller;

import com.SGH.hospital.dto.auth.*;
import com.SGH.hospital.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    // ============================================================
    // Registro
    // ============================================================
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    // ============================================================
    // Logueo
    // ============================================================
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
    
    // ============================================================
    // Refrescar Token
    // ============================================================
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        AuthResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }
    
    // ============================================================
    // Obtener datos del usuario actual
    // ============================================================
    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getCurrentUser() {
        UserInfoResponse response = authService.getCurrentUser();
        return ResponseEntity.ok(response);
    }
}