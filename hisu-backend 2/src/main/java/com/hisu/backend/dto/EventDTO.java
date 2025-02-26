package com.hisu.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private String id;
    private String name;
    private String date;
    private String location;
    private String description;
}
