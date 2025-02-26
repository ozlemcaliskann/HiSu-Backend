package com.hisu.backend.services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.hisu.backend.models.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final Firestore firestore;
    private static final String COLLECTION_NAME = "comments";

    public String addComment(Comment comment) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document();
        comment.setId(docRef.getId());
        ApiFuture<WriteResult> writeResult = docRef.set(comment);
        return writeResult.get().getUpdateTime().toString();
    }

    public List<Comment> getCommentsByTarget(String targetId) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> query = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("targetId", targetId)
                .get();
        return query.get().getDocuments().stream()
                .map(doc -> doc.toObject(Comment.class))
                .collect(Collectors.toList());
    }

    public void deleteComment(String commentId) {
        firestore.collection(COLLECTION_NAME).document(commentId).delete();
    }
}