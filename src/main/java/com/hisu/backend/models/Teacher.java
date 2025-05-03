package com.hisu.backend.models;

import com.google.cloud.firestore.annotation.DocumentId;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Teacher {
    @DocumentId
    private String id;
    private String name;
    private String title;
    private String phoneNumber;
    private String email;
    private String field;
    private String faculty;
    private String imageUrl;
}