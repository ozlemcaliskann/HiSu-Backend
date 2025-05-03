package com.hisu.backend.services;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.hisu.backend.models.Club;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class ClubService {
    private final Firestore firestore;
    private static final String COLLECTION_NAME = "clubs";

    @Autowired
    public ClubService(Firestore firestore) {
        this.firestore = firestore;
    }

    public String createClub(Club club) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document();
        club.setId(docRef.getId());
        club.setCreatedAt(Timestamp.now());

        firestore.collection(COLLECTION_NAME).document(club.getId()).set(club).get();
        return club.getId();
    }

    public List<Club> getAllClubs() throws ExecutionException, InterruptedException {
        return firestore.collection(COLLECTION_NAME)
                .get().get().getDocuments().stream()
                .map(doc -> doc.toObject(Club.class))
                .collect(Collectors.toList());
    }

    public Club getClubById(String clubId) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = firestore.collection(COLLECTION_NAME).document(clubId).get().get();
        if (document.exists()) {
            return document.toObject(Club.class);
        }
        return null;
    }

    public void updateClub(String clubId, Map<String, Object> updates)
            throws ExecutionException, InterruptedException {

        DocumentSnapshot document = firestore.collection(COLLECTION_NAME).document(clubId).get().get();
        if (!document.exists()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Club not found");
        }

        firestore.collection(COLLECTION_NAME).document(clubId).update(updates).get();
    }

    public void deleteClub(String clubId) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME).document(clubId).delete().get();
    }

    public List<Club> getClubsByCategory(String category) throws ExecutionException, InterruptedException {
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("category", category)
                .get().get();

        return querySnapshot.getDocuments().stream()
                .map(doc -> doc.toObject(Club.class))
                .collect(Collectors.toList());
    }
}