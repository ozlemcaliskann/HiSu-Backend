package com.hisu.backend.services;

import com.google.cloud.firestore.*;
import com.hisu.backend.models.Teacher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class TeacherService {
    private final Firestore firestore;
    private static final String COLLECTION_NAME = "teachers";

    public TeacherService(Firestore firestore) {
        this.firestore = firestore;
    }

    public String createTeacher(Teacher teacher) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document();
        teacher.setId(docRef.getId());

        firestore.collection(COLLECTION_NAME).document(teacher.getId()).set(teacher).get();
        return teacher.getId();
    }

    public Teacher getTeacherById(String teacherId) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = firestore.collection(COLLECTION_NAME).document(teacherId).get().get();
        if (document.exists()) {
            return document.toObject(Teacher.class);
        }
        return null;
    }

    public List<Teacher> getAllTeachers() throws ExecutionException, InterruptedException {
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME).get().get();
        return querySnapshot.getDocuments().stream()
                .map(doc -> doc.toObject(Teacher.class))
                .collect(Collectors.toList());
    }

    public List<Teacher> getTeachersByFaculty(String faculty) throws ExecutionException, InterruptedException {
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("faculty", faculty)
                .get().get();

        return querySnapshot.getDocuments().stream()
                .map(doc -> doc.toObject(Teacher.class))
                .collect(Collectors.toList());
    }

    public List<Teacher> getTeachersByField(String field) throws ExecutionException, InterruptedException {
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("field", field)
                .get().get();

        return querySnapshot.getDocuments().stream()
                .map(doc -> doc.toObject(Teacher.class))
                .collect(Collectors.toList());
    }

    public List<Teacher> searchTeachers(String searchTerm) throws ExecutionException, InterruptedException {
        // Note: Firestore doesn't support native full-text search
        // For a production app, consider using Algolia or ElasticSearch
        // This is a simple implementation using prefix matching

        String searchTermLower = searchTerm.toLowerCase();

        // Get all teachers and filter in memory
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME).get().get();

        return querySnapshot.getDocuments().stream()
                .map(doc -> doc.toObject(Teacher.class))
                .filter(teacher ->
                        teacher.getName().toLowerCase().contains(searchTermLower) ||
                                (teacher.getField() != null && teacher.getField().toLowerCase().contains(searchTermLower)) ||
                                (teacher.getEmail() != null && teacher.getEmail().toLowerCase().contains(searchTermLower))
                )
                .collect(Collectors.toList());
    }

    public void updateTeacher(String teacherId, Map<String, Object> updates)
            throws ExecutionException, InterruptedException {

        DocumentSnapshot document = firestore.collection(COLLECTION_NAME).document(teacherId).get().get();
        if (!document.exists()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found");
        }

        firestore.collection(COLLECTION_NAME).document(teacherId).update(updates).get();
    }

    public void deleteTeacher(String teacherId) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = firestore.collection(COLLECTION_NAME).document(teacherId).get().get();
        if (!document.exists()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found");
        }

        firestore.collection(COLLECTION_NAME).document(teacherId).delete().get();
    }
}