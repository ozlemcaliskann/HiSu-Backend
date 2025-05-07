package com.hisu.backend.controllers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.hisu.backend.models.Comment;
import com.hisu.backend.services.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comments")
    public ResponseEntity<String> createComment(
            @RequestAttribute("firebaseUid") String userId,
            @RequestBody Comment comment) throws ExecutionException, InterruptedException {

        comment.setUserId(userId);
        try {
            comment.setUserEmail(
                FirebaseAuth.getInstance().getUser(userId).getEmail()
            );
        } catch (FirebaseAuthException e) {
            // Log the error but continue - email is optional
            System.err.println("Error fetching user email: " + e.getMessage());
        }

        String commentId = commentService.create(comment);
        return ResponseEntity.ok(commentId);
    }

    @GetMapping("/comments/target")
    public ResponseEntity<List<Comment>> getCommentsByTarget(
            @RequestParam Comment.TargetType targetType,
            @RequestParam String targetId) throws ExecutionException, InterruptedException {

        List<Comment> comments = commentService.getCommentsByTarget(targetType, targetId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable String id)
            throws ExecutionException, InterruptedException {

        Comment comment = commentService.getCommentById(id);
        if (comment != null) {
            return ResponseEntity.ok(comment);
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/comments/{id}")
    public ResponseEntity<Void> updateComment(
            @RequestAttribute("firebaseUid") String userId,
            @PathVariable String id,
            @RequestBody Map<String, Object> updates) throws ExecutionException, InterruptedException {

        commentService.update(id, userId, updates);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(
            @RequestAttribute("firebaseUid") String userId,
            @PathVariable String id) throws ExecutionException, InterruptedException {

        commentService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }
}