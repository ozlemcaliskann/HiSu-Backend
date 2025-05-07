package com.hisu.backend.services;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.hisu.backend.models.Comment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final Firestore firestore;
    private final UserService userService;
    private static final String COLLECTION_NAME = "comments";

    public CommentService(Firestore firestore, UserService userService) {
        this.firestore = firestore;
        this.userService = userService;
    }

    public String create(Comment comment) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document();
        comment.setId(docRef.getId());
        Timestamp now = Timestamp.now();
        comment.setCreatedAt(now);
        comment.setUpdatedAt(now);

        firestore.collection(COLLECTION_NAME).document(comment.getId()).set(comment).get();
        return comment.getId();
    }

    public Comment getCommentById(String commentId) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = firestore.collection(COLLECTION_NAME).document(commentId).get().get();
        if (document.exists()) {
            return document.toObject(Comment.class);
        }
        return null;
    }

    public List<Comment> getCommentsByTarget(Comment.TargetType targetType, String targetId)
            throws ExecutionException, InterruptedException {

        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("targetType", targetType)
                .whereEqualTo("targetId", targetId)
                .get().get();

        return querySnapshot.getDocuments().stream()
                .map(doc -> doc.toObject(Comment.class))
                .collect(Collectors.toList());
    }

    public void update(String commentId, String userId, Map<String, Object> updates)
            throws ExecutionException, InterruptedException {

        // Verify the user owns this comment
        DocumentSnapshot doc = firestore.collection(COLLECTION_NAME).document(commentId).get().get();
        if (!doc.exists()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found");
        }

        Comment comment = doc.toObject(Comment.class);
        if (comment == null || !comment.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only update your own comments");
        }

        // Add the updated timestamp
        updates.put("updatedAt", Timestamp.now());

        firestore.collection(COLLECTION_NAME).document(commentId).update(updates).get();
    }

    public void delete(String commentId, String userId) throws ExecutionException, InterruptedException {
        // Verify the user owns this comment
        DocumentSnapshot doc = firestore.collection(COLLECTION_NAME).document(commentId).get().get();
        if (!doc.exists()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found");
        }

        Comment comment = doc.toObject(Comment.class);
        if (comment == null || !comment.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete your own comments");
        }

        firestore.collection(COLLECTION_NAME).document(commentId).delete().get();
    }
}