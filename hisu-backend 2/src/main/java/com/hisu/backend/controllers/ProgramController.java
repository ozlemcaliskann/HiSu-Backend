package com.hisu.backend.controllers;

import com.hisu.backend.models.AcademicProgram;
import com.hisu.backend.services.ProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/programs")
@RequiredArgsConstructor
public class ProgramController {

    private final ProgramService programService;

    @PostMapping
    public ResponseEntity<String> createProgram(@RequestBody AcademicProgram program) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(programService.createProgram(program));
    }

    @GetMapping
    public ResponseEntity<List<AcademicProgram>> getAllPrograms() throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(programService.getAllPrograms());
    }
}
// Compare this snippet from src/main/java/com/hisu/backend/controllers/ProgramController.java:
