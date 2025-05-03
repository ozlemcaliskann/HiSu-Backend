package com.hisu.backend.models;

import com.google.cloud.firestore.annotation.DocumentId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProspectiveStudent {
    @DocumentId
    private String id;
    private String name;
    private String email;
    private String phone;
    private String interest;
}
