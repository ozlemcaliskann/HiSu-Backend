package com.hisu.backend.controllers;

import com.hisu.backend.models.Teacher;
import com.hisu.backend.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {
    private final TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @PostMapping
    public ResponseEntity<String> createTeacher(
            @RequestAttribute(value = "firebaseUid", required = false) String userId,
            @RequestBody Teacher teacher) throws ExecutionException, InterruptedException {

        String teacherId = teacherService.createTeacher(teacher);
        return ResponseEntity.ok(teacherId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable String id)
            throws ExecutionException, InterruptedException {

        Teacher teacher = teacherService.getTeacherById(id);
        if (teacher != null) {
            return ResponseEntity.ok(teacher);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Teacher>> getAllTeachers() throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(teacherService.getAllTeachers());
    }

    @GetMapping("/faculty/{faculty}")
    public ResponseEntity<List<Teacher>> getTeachersByFaculty(@PathVariable String faculty)
            throws ExecutionException, InterruptedException {

        return ResponseEntity.ok(teacherService.getTeachersByFaculty(faculty));
    }

    @GetMapping("/field/{field}")
    public ResponseEntity<List<Teacher>> getTeachersByField(@PathVariable String field)
            throws ExecutionException, InterruptedException {

        return ResponseEntity.ok(teacherService.getTeachersByField(field));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Teacher>> searchTeachers(@RequestParam String query)
            throws ExecutionException, InterruptedException {

        return ResponseEntity.ok(teacherService.searchTeachers(query));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateTeacher(
            @PathVariable String id,
            @RequestBody Map<String, Object> updates) throws ExecutionException, InterruptedException {

        teacherService.updateTeacher(id, updates);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable String id)
            throws ExecutionException, InterruptedException {

        teacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }
}