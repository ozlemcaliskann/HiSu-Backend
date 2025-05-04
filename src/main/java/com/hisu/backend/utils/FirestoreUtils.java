package com.hisu.backend.utils;

import com.google.cloud.firestore.DocumentSnapshot;

public class FirestoreUtils {

    public static boolean documentExists(DocumentSnapshot document) {
        return document != null && document.exists();
    }
}
