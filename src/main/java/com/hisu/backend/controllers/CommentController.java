package com.hisu.backend.controllers;

import com.hisu.backend.models.Comment;
import com.hisu.backend.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<String> createComment(
            @RequestAttribute("firebaseUid") String userId,
            @RequestBody Comment comment) throws ExecutionException, InterruptedException {

        comment.setUserId(userId);


        String commentId = commentService.create(comment);
        return ResponseEntity.ok(commentId);
    }

    @GetMapping("/target")
    public ResponseEntity<List<Comment>> getCommentsByTarget(
            @RequestParam Comment.TargetType targetType,
            @RequestParam String targetId) throws ExecutionException, InterruptedException {

        List<Comment> comments = commentService.getCommentsByTarget(targetType, targetId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable String id)
            throws ExecutionException, InterruptedException {

        Comment comment = commentService.getCommentById(id);
        if (comment != null) {
            return ResponseEntity.ok(comment);
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateComment(
            @RequestAttribute("firebaseUid") String userId,
            @PathVariable String id,
            @RequestBody Map<String, Object> updates) throws ExecutionException, InterruptedException {

        commentService.update(id, userId, updates);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(
            @RequestAttribute("firebaseUid") String userId,
            @PathVariable String id) throws ExecutionException, InterruptedException {

        commentService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }
}