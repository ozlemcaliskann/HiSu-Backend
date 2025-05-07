package com.hisu.backend.services;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.hisu.backend.models.Venue;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class VenueService {
    private final Firestore firestore;
    private static final String COLLECTION_NAME = "venues";

    public VenueService(Firestore firestore) {
        this.firestore = firestore;
    }

    public String createVenue(Venue venue) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document();
        venue.setId(docRef.getId());
        venue.setCreatedAt(Timestamp.now());
        firestore.collection(COLLECTION_NAME).document(venue.getId()).set(venue).get();
        return venue.getId();
    }

    public List<Venue> getAllVenues() throws ExecutionException, InterruptedException {
        return firestore.collection(COLLECTION_NAME).get().get().getDocuments().stream()
                .map(doc -> doc.toObject(Venue.class))
                .collect(Collectors.toList());
    }

    public Venue getVenueById(String venueId) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = firestore.collection(COLLECTION_NAME).document(venueId).get().get();
        if (document.exists()) {
            return document.toObject(Venue.class);
        }
        return null;
    }

    public void updateVenue(String venueId, Map<String, Object> updates) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME).document(venueId).update(updates).get();
    }

    public void deleteVenue(String venueId) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME).document(venueId).delete().get();
    }
}