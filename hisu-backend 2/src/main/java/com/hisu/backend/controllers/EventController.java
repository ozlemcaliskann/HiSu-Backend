package com.hisu.backend.controllers;

import com.hisu.backend.models.Event;
import com.hisu.backend.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<String> createEvent(@RequestBody Event event) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(eventService.createEvent(event));
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable String id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok("Event deleted successfully");
    }
}
