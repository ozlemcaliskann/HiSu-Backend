package com.hisu.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private String id;
    private String email;
    private String role;
    private String name; // Ensure this exists, or remove from constructor

    // Add a constructor with three parameters
    public UserResponseDTO(String id, String email, String role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }
}
