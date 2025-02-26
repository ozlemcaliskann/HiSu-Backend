package com.hisu.backend.services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.hisu.backend.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final Firestore firestore;

    private static final String COLLECTION_NAME = "users";

    public String createUser(User user) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(user.getId());
        ApiFuture<WriteResult> writeResult = docRef.set(user);
        return writeResult.get().getUpdateTime().toString();
    }

    public User getUserById(String userId) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = firestore.collection(COLLECTION_NAME).document(userId).get().get();
        if (document.exists()) {
            return document.toObject(User.class);
        }
        return null;
    }

    public void deleteUser(String userId) {
        firestore.collection(COLLECTION_NAME).document(userId).delete();
    }
}
