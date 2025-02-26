package com.hisu.backend.controllers;

import com.hisu.backend.models.ProspectiveStudent;
import com.hisu.backend.services.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping("/submit")
    public ResponseEntity<String> submitContact(@RequestBody ProspectiveStudent contact) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(contactService.submitContact(contact));
    }

    @GetMapping("/leads")
    public ResponseEntity<List<ProspectiveStudent>> getAllContacts() throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(contactService.getAllContacts());
    }
}

