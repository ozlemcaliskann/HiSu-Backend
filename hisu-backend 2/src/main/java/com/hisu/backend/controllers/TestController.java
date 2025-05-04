package com.hisu.backend.controllers;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Server is running");
        
        // Add Firebase project info for debugging
        try {
            FirebaseApp app = FirebaseApp.getInstance();
            response.put("firebase_project_id", app.getOptions().getProjectId());
            response.put("firebase_initialized", true);
        } catch (Exception e) {
            response.put("firebase_initialized", false);
            response.put("firebase_error", e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-token")
    public ResponseEntity<Map<String, Object>> verifyToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        Map<String, Object> response = new HashMap<>();
        
        if (authHeader == null || authHeader.isEmpty()) {
            response.put("error", "No Authorization header provided");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        
        try {
            String idToken = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
            
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            
            response.put("valid", true);
            response.put("uid", decodedToken.getUid());
            response.put("email", decodedToken.getEmail());
            response.put("is_email_verified", decodedToken.isEmailVerified());
            //response.put("auth_time", decodedToken.getAuthTime());
            response.put("issuer", decodedToken.getIssuer());
            //response.put("token_expiration", decodedToken.getExpirationTime());
            //esponse.put("token_issued_at", decodedToken.getIssuedAtTime());
            
            // Return claims for debugging
            response.put("claims", decodedToken.getClaims());
            
            return ResponseEntity.ok(response);
        } catch (FirebaseAuthException e) {
            response.put("valid", false);
            response.put("error", e.getMessage());
            response.put("error_code", e.getErrorCode());
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (Exception e) {
            response.put("valid", false);
            response.put("error", "Unexpected error: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}