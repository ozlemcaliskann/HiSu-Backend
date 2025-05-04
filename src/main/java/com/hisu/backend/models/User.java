package com.hisu.backend.models;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
    @DocumentId
    private String uid;
    private String email;
    private String displayName;
    private String role;
    private boolean isActive;
    private Timestamp createdAt;

    public User(String email, String displayName) {
        this.email = email;
        this.displayName = displayName;
        this.role = "PRIVATE";
        this.isActive = true;
        this.createdAt = Timestamp.now();
    }
}