package com.hisu.backend.controllers;

import com.hisu.backend.dto.AuthResponse;
import com.hisu.backend.models.User;
import com.hisu.backend.services.AuthService; // Fix the package path
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody Map<String, String> credentials) {
        try {
            String idToken = credentials.get("idToken");
            User user = authService.authenticateUser(idToken);

            return ResponseEntity.ok(new AuthResponse(
                    true,
                    "Login successful",
                    user,
                    idToken
            ));
        } catch (BadCredentialsException e) {
            return ResponseEntity.ok(new AuthResponse(
                    false,
                    e.getMessage(),
                    null,
                    null
            ));
        }
    }
}