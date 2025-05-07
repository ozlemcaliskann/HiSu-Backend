package com.hisu.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic API response wrapper to provide a consistent structure for all responses.
 *
 * @param <T> The type of the response data.
 */
@Data
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    // ✅ Add an explicit constructor that matches the error in AuthController
    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
