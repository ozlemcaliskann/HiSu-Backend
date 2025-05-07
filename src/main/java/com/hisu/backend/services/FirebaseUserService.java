package com.hisu.backend.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FirebaseUserService {
    private final FirebaseAuth firebaseAuth;

    // Constructor Injection to Initialize FirebaseAuth
    public FirebaseUserService(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    /**
     * Assigns a role to a user based on their email.
     * If the email ends with "@sabanciuniv.edu", they get "PRIVATE", otherwise "PUBLIC".
     */
    public void assignRoleBasedOnEmail(String uid, String email) throws FirebaseAuthException {
        String role = email.endsWith("@sabanciuniv.edu") ? "PRIVATE" : "PUBLIC";

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        firebaseAuth.setCustomUserClaims(uid, claims);
    }
}
