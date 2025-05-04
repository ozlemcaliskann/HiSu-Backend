package com.hisu.backend.services;

import com.hisu.backend.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {

    private final FirestoreService firestoreService;
    private static final String COLLECTION_NAME = "users";

    @Autowired
    public UserService(FirestoreService firestoreService) {
        this.firestoreService = firestoreService;
    }

    /**
     * Get user by their ID
     */
    public User getUserById(String uid) throws ExecutionException, InterruptedException {
        return firestoreService.getUser(uid);
    }

    /**
     * Get user by email
     */
    public User getUserByEmail(String email) throws ExecutionException, InterruptedException {
        return firestoreService.getUserByEmail(email);
    }

    /**
     * Update a user's information
     */
    public User updateUser(String uid, User user) throws ExecutionException, InterruptedException {
        user.setUid(uid); // Ensure UID matches path parameter
        firestoreService.saveUser(user);
        return user;
    }

    /**
     * Get all users from the database
     */
    public List<User> getAllUsers() throws ExecutionException, InterruptedException {
        return firestoreService.getAllDocuments(COLLECTION_NAME, User.class);
    }

    /**
     * Save a new user
     */
    public User saveUser(User user) throws ExecutionException, InterruptedException {
        firestoreService.saveUser(user);
        return user;
    }

    /**
     * Delete a user by ID
     */
    public void deleteUser(String uid) throws ExecutionException, InterruptedException {
        firestoreService.deleteUser(uid);
    }

    /**
     * Update specific fields of a user
     */
    public User updateUserFields(String uid, Map<String, Object> fields)
            throws ExecutionException, InterruptedException {
        firestoreService.updateUserFields(uid, fields);
        return getUserById(uid);
    }
}