package com.hisu.backend.controllers;

import com.hisu.backend.models.Comment;
import com.hisu.backend.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @PostMapping
    public ResponseEntity<String> addComment(@RequestBody Comment comment) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(commentService.addComment(comment));
    }

    @GetMapping("/{targetId}")
    public ResponseEntity<List<Comment>> getCommentsByTarget(@PathVariable String targetId) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(commentService.getCommentsByTarget(targetId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable String id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok("Comment deleted successfully");
    }
}
