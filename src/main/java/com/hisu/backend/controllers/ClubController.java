package com.hisu.backend.controllers;

import com.hisu.backend.models.Club;
import com.hisu.backend.services.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/clubs")
public class ClubController {
    private final ClubService clubService;

    @Autowired
    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    @PostMapping
    public ResponseEntity<String> createClub(
            @RequestAttribute("firebaseUid") String userId,
            @RequestBody Club club) throws ExecutionException, InterruptedException {

        club.setCreatedBy(userId);
        String clubId = clubService.createClub(club);
        return ResponseEntity.ok(clubId);
    }

    @GetMapping
    public ResponseEntity<List<Club>> getAllClubs() throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(clubService.getAllClubs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Club> getClubById(@PathVariable String id)
            throws ExecutionException, InterruptedException {

        Club club = clubService.getClubById(id);
        if (club != null) {
            return ResponseEntity.ok(club);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Club>> getClubsByCategory(@PathVariable String category)
            throws ExecutionException, InterruptedException {

        return ResponseEntity.ok(clubService.getClubsByCategory(category));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateClub(
            @PathVariable String id,
            @RequestBody Map<String, Object> updates) throws ExecutionException, InterruptedException {

        clubService.updateClub(id, updates);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClub(@PathVariable String id)
            throws ExecutionException, InterruptedException {

        clubService.deleteClub(id);
        return ResponseEntity.noContent().build();
    }
}