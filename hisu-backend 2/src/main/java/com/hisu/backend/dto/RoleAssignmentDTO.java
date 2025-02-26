package com.hisu.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleAssignmentDTO {
    private String userId;
    private String role;
    private boolean success;
}
