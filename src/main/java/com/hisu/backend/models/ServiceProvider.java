package com.hisu.backend.models;

import com.google.cloud.Timestamp;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class ServiceProvider {
    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private String websiteUrl;
    private String contactEmail;
    private String contactPhone;
    private Map<String, Boolean> services;
    private String category;
    private String location;
    private Map<String, Object> locationData;
    private Timestamp createdAt;
    private boolean verified;

    // Minimum yapıcı metod
    public ServiceProvider(String id, String name, String description, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.verified = true;
    }
} 