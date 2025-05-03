package com.hisu.backend.services;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.hisu.backend.models.Like;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class LikeService {
    private final Firestore firestore;
    private static final String COLLECTION_NAME = "likes";

    @Autowired
    public LikeService(Firestore firestore) {
        this.firestore = firestore;
    }

    public String addLike(Like like) throws ExecutionException, InterruptedException {
        // Check if the like already exists
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("userId", like.getUserId())
                .whereEqualTo("targetType", like.getTargetType())
                .whereEqualTo("targetId", like.getTargetId())
                .limit(1)
                .get().get();

        if (!querySnapshot.isEmpty()) {
            // User has already liked this target
            return querySnapshot.getDocuments().get(0).getId();
        }

        // Create new like
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document();
        like.setId(docRef.getId());
        like.setCreatedAt(Timestamp.now());

        firestore.collection(COLLECTION_NAME).document(like.getId()).set(like).get();
        return like.getId();
    }

    public void removeLike(String userId, Like.TargetType targetType, String targetId)
            throws ExecutionException, InterruptedException {

        // Find the like document
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId)
                .whereEqualTo("targetType", targetType)
                .whereEqualTo("targetId", targetId)
                .limit(1)
                .get().get();

        if (querySnapshot.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Like not found");
        }

        // Delete the like document
        String likeId = querySnapshot.getDocuments().get(0).getId();
        firestore.collection(COLLECTION_NAME).document(likeId).delete().get();
    }

    public boolean hasUserLiked(String userId, Like.TargetType targetType, String targetId)
            throws ExecutionException, InterruptedException {

        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId)
                .whereEqualTo("targetType", targetType)
                .whereEqualTo("targetId", targetId)
                .limit(1)
                .get().get();

        return !querySnapshot.isEmpty();
    }

    public long getLikeCount(Like.TargetType targetType, String targetId)
            throws ExecutionException, InterruptedException {

        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("targetType", targetType)
                .whereEqualTo("targetId", targetId)
                .get().get();

        return querySnapshot.size();
    }

    public List<Like> getLikesByUser(String userId) throws ExecutionException, InterruptedException {
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId)
                .get().get();

        return querySnapshot.getDocuments().stream()
                .map(doc -> doc.toObject(Like.class))
                .collect(Collectors.toList());
    }
}