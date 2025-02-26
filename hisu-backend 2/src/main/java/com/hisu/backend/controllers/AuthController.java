package com.hisu.backend.controllers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.hisu.backend.dto.ApiResponse;
import com.hisu.backend.dto.RoleAssignmentDTO;
import com.hisu.backend.dto.UserResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @GetMapping("/user/{uid}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserInfo(@PathVariable String uid) {
        try {
            UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);
            String role = (String) userRecord.getCustomClaims().getOrDefault("role", "PUBLIC");

            UserResponseDTO userResponse = new UserResponseDTO(
                    userRecord.getUid(),
                    userRecord.getEmail(),
                    role,
                    userRecord.getDisplayName()
            );

            logger.info("Fetched user info: UID={}", uid);
            return ResponseEntity.ok(new ApiResponse<>(true, "User details fetched", userResponse));
        } catch (FirebaseAuthException e) {
            logger.error("Error fetching user info: UID={}", uid, e);
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Error fetching user info", null));
        }
    }

    @PostMapping("/assign-role")
    public ResponseEntity<ApiResponse<RoleAssignmentDTO>> assignRole(@RequestParam String uid, @RequestParam String role) {
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", role);

            FirebaseAuth.getInstance().setCustomUserClaims(uid, claims);
            logger.info("Role assigned: UID={} Role={}", uid, role);

            RoleAssignmentDTO roleResponse = new RoleAssignmentDTO(uid, role, true);
            return ResponseEntity.ok(new ApiResponse<>(true, "Role assigned successfully", roleResponse));
        } catch (FirebaseAuthException e) {
            logger.error("Error assigning role: UID={}", uid, e);
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Error assigning role", null));
        }
    }
}
