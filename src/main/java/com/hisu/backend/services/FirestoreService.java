package com.hisu.backend.services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.hisu.backend.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.google.firebase.cloud.FirestoreClient.getFirestore;

@Service
public class FirestoreService {

    private final Firestore firestore;
    private static final String COLLECTION_USERS = "users";

    @Autowired
    public FirestoreService(Firestore firestore) {
        this.firestore = firestore;
    }

    /**
     * Saves a user to Firestore.
     */
    public void saveUser(User user) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_USERS).document(user.getUid());
        docRef.set(user).get();
    }


    /**
     * Retrieves a user by UID from Firestore.
     */
    public User getUser(String uid) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_USERS).document(uid);
        DocumentSnapshot document = docRef.get().get();
        return document.exists() ? document.toObject(User.class) : null;
    }

    /**
     * Updates specific fields of a user document.
     */
    public void updateUserFields(String uid, Map<String, Object> updates) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_USERS).document(uid);
        ApiFuture<WriteResult> writeResult = docRef.update(updates);
        writeResult.get();
    }

    /**
     * Deletes a user document.
     */
    public void deleteUser(String uid) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_USERS).document(uid);
        docRef.delete().get();
    }

    /**
     * Generic method to get all documents from a collection.
     */
    public <T> List<T> getAllDocuments(String collectionName, Class<T> valueType) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(collectionName).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        return documents.stream()
                .map(document -> document.toObject(valueType))
                .collect(Collectors.toList());
    }

    /**
     * Generic method to save a document with auto-generated ID.
     */
    public <T> String saveDocument(String collectionName, T data) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(collectionName).document();
        ApiFuture<WriteResult> result = docRef.set(data);
        result.get();
        return docRef.getId();
    }
    /**
     * Get user by email
     */
    public User getUserByEmail(String email) throws ExecutionException, InterruptedException {
        QuerySnapshot querySnapshot = getFirestore().collection("users")
                .whereEqualTo("email", email)
                .get()
                .get();

        if (querySnapshot.isEmpty()) {
            return null;
        }

        return querySnapshot.getDocuments().get(0).toObject(User.class);
    }

    // For fetching documents by field
    public <T> T getDocumentByField(String collectionName, String field, String value, Class<T> valueType)
            throws ExecutionException, InterruptedException {

        QuerySnapshot querySnapshot = FirestoreClient.getFirestore()
                .collection(collectionName)
                .whereEqualTo(field, value)
                .limit(1)
                .get()
                .get();

        if (querySnapshot.isEmpty()) {
            return null;
        }

        return querySnapshot.getDocuments().get(0).toObject(valueType);
    }




}