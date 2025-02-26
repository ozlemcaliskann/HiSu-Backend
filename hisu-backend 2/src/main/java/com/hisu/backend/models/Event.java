package com.hisu.backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import com.google.cloud.firestore.annotation.DocumentId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @DocumentId
    private String id;
    private String title;
    private String description;
    private String clubId;
    private Date date;
}
