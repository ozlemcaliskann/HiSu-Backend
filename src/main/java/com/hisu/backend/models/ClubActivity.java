package com.hisu.backend.models;

import com.google.cloud.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClubActivity {
    private String id;
    private String clubId;
    private String clubName;
    private String activityName;
    private String date;
    private String location;
    private String icon;
    private String description;
    private Timestamp createdAt;
    // Add any other fields you need
} 