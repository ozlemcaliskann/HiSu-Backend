package com.hisu.backend.controllers;

import com.hisu.backend.models.ClubActivity;
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
@RequestMapping("/api")
public class ActivityController {

    private static final Logger logger = LoggerFactory.getLogger(ActivityController.class);
    
    @Autowired
    private ClubService clubService;

    // Get all activities
    @GetMapping("/activities")
    public ResponseEntity<?> getAllActivities() {
        logger.debug("GET /api/activities called");
        try {
            List<ClubActivity> activities = clubService.getAllActivities();
            logger.info("Returning {} activities", activities.size());
            return ResponseEntity.ok(activities);
        } catch (ExecutionException | InterruptedException e) {
            logger.error("Error getting all activities: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error fetching activities: " + e.getMessage()));
        }
    }

    // Get activities for a specific club
    @GetMapping("/clubs/{clubId}/activities")
    public ResponseEntity<?> getClubActivities(@PathVariable String clubId) {
        logger.debug("GET /api/clubs/{}/activities called", clubId);
        try {
            List<ClubActivity> activities = clubService.getActivitiesByClubId(clubId);
            logger.info("Returning {} activities for club ID: {}", activities.size(), clubId);
            return ResponseEntity.ok(activities);
        } catch (ExecutionException | InterruptedException e) {
            logger.error("Error getting activities for club {}: {}", clubId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error fetching club activities: " + e.getMessage()));
        }
    }

    // Get activity by ID
    @GetMapping("/activities/{id}")
    public ResponseEntity<?> getActivityById(@PathVariable String id) {
        logger.debug("GET /api/activities/{} called", id);
        try {
            ClubActivity activity = clubService.getActivityById(id);
            if (activity != null) {
                logger.info("Activity found: {}", activity.getActivityName());
                return ResponseEntity.ok(activity);
            } else {
                logger.warn("Activity not found with ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseMessage("Activity not found with id: " + id));
            }
        } catch (ExecutionException | InterruptedException e) {
            logger.error("Error getting activity with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error fetching activity: " + e.getMessage()));
        }
    }
} 