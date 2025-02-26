package com.hisu.backend.services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.hisu.backend.models.AcademicProgram;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgramService {

    private final Firestore firestore;
    private static final String COLLECTION_NAME = "programs";

    public String createProgram(AcademicProgram program) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(program.getId());
        ApiFuture<WriteResult> writeResult = docRef.set(program);
        return writeResult.get().getUpdateTime().toString();
    }

    public List<AcademicProgram> getAllPrograms() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> query = firestore.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = query.get().getDocuments();
        return documents.stream().map(doc -> doc.toObject(AcademicProgram.class)).collect(Collectors.toList());
    }
}
