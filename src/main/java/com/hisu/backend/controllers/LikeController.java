package com.hisu.backend.controllers;

import com.hisu.backend.models.Like;
import com.hisu.backend.models.ResponseMessage;
import com.hisu.backend.services.LikeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping
    public ResponseEntity<ResponseMessage> addLike(@RequestBody Like like, HttpServletRequest request) {
        try {
            // Get the UID from the request attributes
            String uid = (String) request.getAttribute("firebaseUid");
            if (uid == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ResponseMessage("Unauthorized"));
            }

            // Set the user ID from the authenticated user
            like.setUserId(uid);

            // Save the like
            String likeId = likeService.addLike(like);

            return ResponseEntity.ok(new ResponseMessage("Like added successfully"));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Error adding like: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{itemType}/{itemId}")
    public ResponseEntity<ResponseMessage> removeLike(
            @PathVariable String itemType,
            @PathVariable String itemId,
            HttpServletRequest request) {
        try {
            // Get the UID from the request attributes
            String uid = (String) request.getAttribute("firebaseUid");
            if (uid == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ResponseMessage("Unauthorized"));
            }

            // Convert string to enum
            Like.TargetType targetType = Like.TargetType.valueOf(itemType.toUpperCase());
            
            // Remove the like
            likeService.removeLike(uid, targetType, itemId);

            return ResponseEntity.ok(new ResponseMessage("Like removed successfully"));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Error removing like: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("Invalid item type: " + itemType));
        }
    }

    @GetMapping("/count/{itemType}/{itemId}")
    public ResponseEntity<Map<String, Object>> getLikeCount(
            @PathVariable String itemType,
            @PathVariable String itemId) {
        try {
            // Convert string to enum
            Like.TargetType targetType = Like.TargetType.valueOf(itemType.toUpperCase());
            
            // Get the count of likes for the item
            long count = likeService.getLikeCount(targetType, itemId);

            // Create a response map
            Map<String, Object> response = new HashMap<>();
            response.put("count", count);

            return ResponseEntity.ok(response);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error getting like count: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Invalid item type: " + itemType));
        }
    }

    @GetMapping("/status/{itemType}/{itemId}")
    public ResponseEntity<?> hasUserLiked(
            @PathVariable String itemType,
            @PathVariable String itemId,
            HttpServletRequest request) {
        try {
            // İsteğe bağlı kullanıcı kimliği kontrolü
            String uid = (String) request.getAttribute("firebaseUid");
            
            // Kullanıcı kimliği yoksa, varsayılan olarak false döndür
            boolean hasLiked = false;
            
            // Kullanıcı kimliği varsa, like durumunu kontrol et
            if (uid != null) {
                // Convert string to enum
                Like.TargetType targetType = Like.TargetType.valueOf(itemType.toUpperCase());
                hasLiked = likeService.hasUserLiked(uid, targetType, itemId);
            }
            
            // Basit yanıt objesi
            Map<String, Object> response = new HashMap<>();
            response.put("hasLiked", hasLiked);
            
            return ResponseEntity.ok(response);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error checking like status: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Invalid item type: " + itemType));
        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<Like>> getUserLikes(@RequestAttribute("firebaseUid") String userId) throws ExecutionException, InterruptedException {
        List<Like> likes = likeService.getLikesByUser(userId);
        return ResponseEntity.ok(likes);
    }
}