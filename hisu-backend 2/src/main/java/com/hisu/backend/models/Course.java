package com.hisu.backend.models;

import com.google.cloud.firestore.annotation.DocumentId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    @DocumentId
    private String id;
    private String programId;
    private String name;
    private String facultyId;
    private String description;
}
