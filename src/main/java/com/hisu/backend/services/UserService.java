package com.hisu.backend.services;

import com.google.cloud.firestore.*;
import com.hisu.backend.models.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final Firestore firestore;
    private static final String COLLECTION_NAME = "users";

    public UserService(Firestore firestore) {
        this.firestore = firestore;
    }

    /**
     * Get user by their ID
     */
    public User getUserById(String uid) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = firestore.collection(COLLECTION_NAME).document(uid).get().get();
        if (document.exists()) {
            return document.toObject(User.class);
        }
        return null;
    }

    /**
     * Get user by email
     */
    public User getUserByEmail(String email) throws ExecutionException, InterruptedException {
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("email", email)
                .limit(1)
                .get().get();
        
        if (!querySnapshot.isEmpty()) {
            return querySnapshot.getDocuments().get(0).toObject(User.class);
        }
        return null;
    }

    /**
     * Update a user's information
     */
    public User updateUser(String uid, User user) throws ExecutionException, InterruptedException {
        user.setUid(uid); // Ensure UID matches path parameter
        firestore.collection(COLLECTION_NAME).document(uid).set(user).get();
        return user;
    }

    /**
     * Get all users from the database
     */
    public List<User> getAllUsers() throws ExecutionException, InterruptedException {
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME).get().get();
        return querySnapshot.getDocuments().stream()
                .map(doc -> doc.toObject(User.class))
                .collect(Collectors.toList());
    }

    /**
     * Save a new user
     */
    public User saveUser(User user) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME).document(user.getUid()).set(user).get();
        return user;
    }

    /**
     * Delete a user by ID
     */
    public void deleteUser(String uid) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME).document(uid).delete().get();
    }

    /**
     * Update specific fields of a user
     */
    public User updateUserFields(String uid, Map<String, Object> fields)
            throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME).document(uid).update(fields).get();
        return getUserById(uid);
    }
}