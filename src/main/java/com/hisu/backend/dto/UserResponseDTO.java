package com.hisu.backend.dto;

public class UserResponseDTO {
    private String uid;
    private String email;
    private String displayName;
    private String role;

    public UserResponseDTO(String uid, String email, String role) {
        this.uid = uid;
        this.email = email;
        this.role = role;
    }

    public UserResponseDTO(String uid, String email, String displayName, String role) {
        this.uid = uid;
        this.email = email;
        this.displayName = displayName;
        this.role = role;
    }

    // Getters and setters
    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}