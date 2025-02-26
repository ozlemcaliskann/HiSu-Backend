package com.hisu.backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class Comment {
    private String id;
    private String userId;
    private String categoryId; // Can be clubId, courseId, eventId
    private String content;
    private long timestamp;
}
