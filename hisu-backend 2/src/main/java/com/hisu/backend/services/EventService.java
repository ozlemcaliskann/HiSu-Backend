package com.hisu.backend.services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.hisu.backend.models.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final Firestore firestore;
    private static final String COLLECTION_NAME = "events";

    public String createEvent(Event event) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(event.getId());
        ApiFuture<WriteResult> writeResult = docRef.set(event);
        return writeResult.get().getUpdateTime().toString();
    }

    public List<Event> getAllEvents() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> query = firestore.collection(COLLECTION_NAME).get();
        return query.get().getDocuments().stream()
                .map(doc -> doc.toObject(Event.class))
                .collect(Collectors.toList());
    }

    public void deleteEvent(String eventId) {
        firestore.collection(COLLECTION_NAME).document(eventId).delete();
    }
}
