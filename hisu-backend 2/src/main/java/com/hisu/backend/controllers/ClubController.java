package com.hisu.backend.controllers;

import com.hisu.backend.models.StudentClub;
import com.hisu.backend.services.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/clubs")
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;

    @PostMapping
    public ResponseEntity<String> createClub(@RequestBody StudentClub club) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(clubService.createClub(club));
    }
}
