package com.hisu.backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.google.cloud.firestore.annotation.DocumentId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @DocumentId
    private String id;
    private String name;
    private String email;
    private String role;  // public, private, admin
}