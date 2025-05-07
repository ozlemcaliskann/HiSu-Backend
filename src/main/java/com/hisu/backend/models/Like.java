package com.hisu.backend.models;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Like {
    @DocumentId
    private String id;
    private String userId;
    private TargetType targetType;
    private String targetId;
    private Timestamp createdAt;

    public enum TargetType {
        VENUE, LESSON, CLUB, SERVICE
    }
}