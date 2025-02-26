package com.hisu.backend.controllers;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/test-firestore")
public class FirebaseTestController {

    @GetMapping
    public String testFirestore() {
        try {
            Firestore db = FirestoreClient.getFirestore();
            DocumentSnapshot document = db.collection("test").document("sampleDoc").get().get();

            if (document.exists()) {
                return "Firestore connection successful! Document data: " + document.getData();
            } else {
                return "Firestore connection successful, but document does not exist.";
            }
        } catch (InterruptedException | ExecutionException e) {
            return "Firestore connection failed: " + e.getMessage();
        }
    }
}
