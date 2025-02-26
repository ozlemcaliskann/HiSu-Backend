package com.hisu.backend.services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.hisu.backend.models.StudentClub;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final Firestore firestore;
    private static final String COLLECTION_NAME = "clubs";

    public String createClub(StudentClub club) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(club.getId());
        ApiFuture<WriteResult> writeResult = docRef.set(club);
        return writeResult.get().getUpdateTime().toString();
    }
}
