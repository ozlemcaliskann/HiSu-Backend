package com.hisu.backend.models;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Comment {
    @DocumentId
    private String id;
    private String userId;
    private String userEmail;
    private TargetType targetType;
    private String targetId;
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public enum TargetType {
        VENUE, LESSON, CLUB, SERVICE
    }
}