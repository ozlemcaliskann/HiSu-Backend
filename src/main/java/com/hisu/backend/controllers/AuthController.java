package com.hisu.backend.controllers;

import com.hisu.backend.dto.AuthResponse;
import com.hisu.backend.models.User;
import com.hisu.backend.services.AuthService; // Fix the package path
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired // Add autowired annotation
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