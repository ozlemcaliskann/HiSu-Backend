package com.hisu.backend.controllers;

import com.hisu.backend.models.Like;
import com.hisu.backend.services.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/likes")
public class LikeController {
    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping
    public ResponseEntity<String> addLike(
            @RequestAttribute("firebaseUid") String userId,
            @RequestBody Like like) throws ExecutionException, InterruptedException {

        like.setUserId(userId);
        String likeId = likeService.addLike(like);
        return ResponseEntity.ok(likeId);
    }

    @DeleteMapping
    public ResponseEntity<Void> removeLike(
            @RequestAttribute("firebaseUid") String userId,
            @RequestParam Like.TargetType targetType,
            @RequestParam String targetId) throws ExecutionException, InterruptedException {

        likeService.removeLike(userId, targetType, targetId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getLikeStatus(
            @RequestAttribute("firebaseUid") String userId,
            @RequestParam Like.TargetType targetType,
            @RequestParam String targetId) throws ExecutionException, InterruptedException {

        boolean hasLiked = likeService.hasUserLiked(userId, targetType, targetId);
        long totalLikes = likeService.getLikeCount(targetType, targetId);

        Map<String, Object> response = new HashMap<>();
        response.put("hasLiked", hasLiked);
        response.put("totalLikes", totalLikes);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Like>> getUserLikes(
            @RequestAttribute("firebaseUid") String userId) throws ExecutionException, InterruptedException {

        List<Like> likes = likeService.getLikesByUser(userId);
        return ResponseEntity.ok(likes);
    }
}