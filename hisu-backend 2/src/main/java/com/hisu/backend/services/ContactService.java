package com.hisu.backend.services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.hisu.backend.models.ProspectiveStudent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final Firestore firestore;
    private static final String COLLECTION_NAME = "prospective_students";

    public String submitContact(ProspectiveStudent contact) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document();
        contact.setId(docRef.getId());
        ApiFuture<WriteResult> writeResult = docRef.set(contact);
        return writeResult.get().getUpdateTime().toString();
    }

    public List<ProspectiveStudent> getAllContacts() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> query = firestore.collection(COLLECTION_NAME).get();
        return query.get().getDocuments().stream()
                .map(doc -> doc.toObject(ProspectiveStudent.class))
                .collect(Collectors.toList());
    }
}
