package com.hisu.backend.models;

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
    // Add any other fields you need
}