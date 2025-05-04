package com.hisu.backend.services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.hisu.backend.models.StudentClub;
import com.hisu.backend.models.ClubActivity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final Firestore firestore;
    private static final String CLUBS_COLLECTION = "clubs";
    private static final String ACTIVITIES_COLLECTION = "activities";

    // Original club methods
    // Create a new club
    public String createClub(StudentClub club) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(CLUBS_COLLECTION).document(club.getId());
        ApiFuture<WriteResult> writeResult = docRef.set(club);
        return writeResult.get().getUpdateTime().toString();
    }

    // Get all clubs
    public List<StudentClub> getAllClubs() throws ExecutionException, InterruptedException {
        CollectionReference clubsRef = firestore.collection(CLUBS_COLLECTION);
        ApiFuture<QuerySnapshot> future = clubsRef.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        List<StudentClub> clubList = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            StudentClub club = doc.toObject(StudentClub.class);
            clubList.add(club);
        }
        return clubList;
    }

    // Get club by ID
    public StudentClub getClubById(String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(CLUBS_COLLECTION).document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        
        if (document.exists()) {
            return document.toObject(StudentClub.class);
        } else {
            return null;
        }
    }

    // Update club
    public String updateClub(String id, StudentClub club) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(CLUBS_COLLECTION).document(id);
        ApiFuture<WriteResult> writeResult = docRef.set(club);
        return writeResult.get().getUpdateTime().toString();
    }

    // Delete club
    public String deleteClub(String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(CLUBS_COLLECTION).document(id);
        ApiFuture<WriteResult> writeResult = docRef.delete();
        return writeResult.get().getUpdateTime().toString();
    }

    // New methods for club activities
    // Create a new activity for a club
    public String createActivity(ClubActivity activity) throws ExecutionException, InterruptedException {
        // Generate a random ID if not provided
        if (activity.getId() == null || activity.getId().isEmpty()) {
            activity.setId(UUID.randomUUID().toString());
        }
        
        // Ensure clubName is set if club exists
        if (activity.getClubName() == null || activity.getClubName().isEmpty()) {
            StudentClub club = getClubById(activity.getClubId());
            if (club != null) {
                activity.setClubName(club.getName());
            }
        }
        
        DocumentReference docRef = firestore.collection(ACTIVITIES_COLLECTION).document(activity.getId());
        ApiFuture<WriteResult> writeResult = docRef.set(activity);
        return writeResult.get().getUpdateTime().toString();
    }

    // Get all activities
    public List<ClubActivity> getAllActivities() throws ExecutionException, InterruptedException {
        CollectionReference activitiesRef = firestore.collection(ACTIVITIES_COLLECTION);
        ApiFuture<QuerySnapshot> future = activitiesRef.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        List<ClubActivity> activityList = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            ClubActivity activity = doc.toObject(ClubActivity.class);
            activityList.add(activity);
        }
        return activityList;
    }

    // Get activities for a specific club
    public List<ClubActivity> getActivitiesByClubId(String clubId) throws ExecutionException, InterruptedException {
        CollectionReference activitiesRef = firestore.collection(ACTIVITIES_COLLECTION);
        Query query = activitiesRef.whereEqualTo("clubId", clubId);
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        List<ClubActivity> activityList = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            ClubActivity activity = doc.toObject(ClubActivity.class);
            activityList.add(activity);
        }
        return activityList;
    }

    // Get activity by ID
    public ClubActivity getActivityById(String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(ACTIVITIES_COLLECTION).document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        
        if (document.exists()) {
            return document.toObject(ClubActivity.class);
        } else {
            return null;
        }
    }

    // Update activity
    public String updateActivity(String id, ClubActivity activity) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(ACTIVITIES_COLLECTION).document(id);
        ApiFuture<WriteResult> writeResult = docRef.set(activity);
        return writeResult.get().getUpdateTime().toString();
    }

    // Delete activity
    public String deleteActivity(String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(ACTIVITIES_COLLECTION).document(id);
        ApiFuture<WriteResult> writeResult = docRef.delete();
        return writeResult.get().getUpdateTime().toString();
    }
}