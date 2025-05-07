package com.hisu.backend.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.hisu.backend.models.User;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.concurrent.ExecutionException;

@Service
public class AuthService {
    private final FirestoreService firestoreService;
    private final FirebaseAuth firebaseAuth;
    private final UserService userService;
    private static final String COLLECTION_NAME = "users";

    public AuthService(FirestoreService firestoreService, FirebaseAuth firebaseAuth, UserService userService) {
        this.firestoreService = firestoreService;
        this.firebaseAuth = firebaseAuth;
        this.userService = userService;
    }

    public User authenticateUser(String idToken) throws BadCredentialsException {
        try {
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(idToken);
            String email = decodedToken.getEmail();
            String uid = decodedToken.getUid();

            if (email == null || !email.endsWith("@sabanciuniv.edu")) {
                throw new BadCredentialsException("Only @sabanciuniv.edu email addresses are allowed");
            }

            // Try to find existing user by email
            User user = firestoreService.getDocumentByField(COLLECTION_NAME, "email", email, User.class);

            if (user == null) {
                // Create new user if not exists
                user = new User(email, decodedToken.getName()); // Using the constructor
                user.setUid(uid);
                // Use saveUser method which is designed for users specifically
                firestoreService.saveUser(user);
            }

            return user;
        } catch (FirebaseAuthException e) {
            throw new BadCredentialsException("Invalid credentials");
        } catch (ExecutionException | InterruptedException e) {
            throw new BadCredentialsException("Database error: " + e.getMessage());
        }
    }
}