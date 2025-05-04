// UserRole.java
package com.hisu.backend.models;

public enum UserRole {
    PUBLIC,
    PRIVATE,
    ADMIN;

    public static UserRole fromEmail(String email) {
        return email != null && email.endsWith("@sabanciuniv.edu") ? PRIVATE : PUBLIC;
    }
}