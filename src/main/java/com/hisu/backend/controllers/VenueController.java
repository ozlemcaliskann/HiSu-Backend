package com.hisu.backend.controllers;

import com.hisu.backend.models.Venue;
import com.hisu.backend.services.VenueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/venues")
public class VenueController {
    private final VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @PostMapping
    public ResponseEntity<String> createVenue(@RequestBody Venue venue) throws ExecutionException, InterruptedException {
        String venueId = venueService.createVenue(venue);
        return ResponseEntity.ok(venueId);
    }

    @GetMapping
    public ResponseEntity<List<Venue>> getAllVenues() throws ExecutionException, InterruptedException {
        List<Venue> venues = venueService.getAllVenues();
        return ResponseEntity.ok(venues);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venue> getVenueById(@PathVariable String id) throws ExecutionException, InterruptedException {
        Venue venue = venueService.getVenueById(id);
        if (venue != null) {
            return ResponseEntity.ok(venue);
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateVenue(@PathVariable String id, @RequestBody Map<String, Object> updates)
            throws ExecutionException, InterruptedException {
        venueService.updateVenue(id, updates);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenue(@PathVariable String id) throws ExecutionException, InterruptedException {
        venueService.deleteVenue(id);
        return ResponseEntity.noContent().build();
    }
}