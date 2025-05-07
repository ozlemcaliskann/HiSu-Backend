package com.hisu.backend.controllers;

import com.hisu.backend.models.Club;
import com.hisu.backend.models.ResponseMessage;
import com.hisu.backend.services.ClubService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/clubs")
public class ClubController {
    
    private static final Logger logger = LoggerFactory.getLogger(ClubController.class);
    @Autowired
    private ClubService clubService;

    @PostMapping
    public ResponseEntity<String> createClub(
            @RequestAttribute("firebaseUid") String userId,
            @RequestBody Club club) throws ExecutionException, InterruptedException {

        logger.info("Creating new club: {}", club.getName());
        try {
            club.setCreatedBy(userId);
            String clubId = clubService.createClub(club);
            logger.info("Club created with ID: {}", clubId);
            return ResponseEntity.ok(clubId);
        } catch (Exception e) {
            logger.error("Error creating club: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllClubs() {
        try {
            List<Club> clubs = clubService.getAllClubs();
            return ResponseEntity.ok(clubs);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error fetching clubs: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClubById(@PathVariable String id) {
        try {
            Club club = clubService.getClubById(id);
            if (club != null) {
                return ResponseEntity.ok(club);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseMessage("Club not found with id: " + id));
            }
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error fetching club: " + e.getMessage()));
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<?> getClubsByCategory(@PathVariable String category) {
        try {
            List<Club> clubs = clubService.getClubsByCategory(category);
            return ResponseEntity.ok(clubs);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error fetching clubs by category: " + e.getMessage()));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateClub(
            @PathVariable String id,
            @RequestBody Map<String, Object> updates) throws ExecutionException, InterruptedException {

        logger.info("Updating club with ID: {}", id);
        try {
            clubService.updateClub(id, updates);
            logger.info("Club updated successfully: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error updating club with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClub(@PathVariable String id)
            throws ExecutionException, InterruptedException {

        logger.info("Deleting club with ID: {}", id);
        try {
            clubService.deleteClub(id);
            logger.info("Club deleted successfully: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting club with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
}