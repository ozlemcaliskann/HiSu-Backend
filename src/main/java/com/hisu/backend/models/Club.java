package com.hisu.backend.models;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Club {
    @DocumentId
    private String id;
    private String name;
    private String description;
    private String meetingInfo;
    private String contactInfo;
    private Timestamp createdAt;
    private String createdBy;
    private String imageUrl;
    private String category;
}