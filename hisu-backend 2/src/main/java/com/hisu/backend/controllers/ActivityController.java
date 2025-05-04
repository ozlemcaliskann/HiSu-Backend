// ActivityController.java
package com.hisu.backend.controllers;

import com.hisu.backend.models.ClubActivity;
import com.hisu.backend.services.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ActivityController {

    private final ClubService clubService;

    // Get all activities
    @GetMapping("/activities")
    public ResponseEntity<List<ClubActivity>> getAllActivities() throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(clubService.getAllActivities());
    }

    // Get activities for a specific club
    @GetMapping("/clubs/{clubId}/activities")
    public ResponseEntity<List<ClubActivity>> getClubActivities(@PathVariable String clubId) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(clubService.getActivitiesByClubId(clubId));
    }

    // Get activity by ID
    @GetMapping("/activities/{id}")
    public ResponseEntity<ClubActivity> getActivityById(@PathVariable String id) throws ExecutionException, InterruptedException {
        ClubActivity activity = clubService.getActivityById(id);
        if (activity != null) {
            return ResponseEntity.ok(activity);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}