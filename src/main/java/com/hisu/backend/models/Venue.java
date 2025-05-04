package com.hisu.backend.models;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Venue {
    @DocumentId
    private String id;
    private String name;
    private String description;
    private String addInfo;
    private String dropInfo;
    private Timestamp createdAt;
    private Double latitude;
    private Double longitude;
}