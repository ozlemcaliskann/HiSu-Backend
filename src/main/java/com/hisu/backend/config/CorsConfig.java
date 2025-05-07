package com.hisu.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Allow credentials
        config.setAllowCredentials(true);

        // Set allowed origin patterns
        config.setAllowedOriginPatterns(Arrays.asList(
                "http://localhost:[*]",
                "http://10.0.2.2:[*]",
                "http://192.168.1.[*]:[*]",    // Yerel ağ IP'leri
                "exp://[*]",                   // Expo development URLs
                "exp://192.168.1.[*]:[*]",     // Expo IP-tabanlı development
                "https://*.hisu.app"           // Production domain (adjust as needed)
        ));

        // Allow all common headers
        config.setAllowedHeaders(Arrays.asList(
                "Origin",
                "Content-Type",
                "Accept",
                "Authorization",
                "Access-Control-Allow-Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers",
                "Access-Control-Allow-Credentials"
        ));

        // Allow all common HTTP methods
        config.setAllowedMethods(Arrays.asList(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
        ));

        // Allow browsers to cache CORS response for 1 hour
        config.setMaxAge(3600L);

        // Apply CORS configuration to all API routes
        source.registerCorsConfiguration("/api/**", config);
        source.registerCorsConfiguration("/auth/**", config);
        source.registerCorsConfiguration("/public/**", config);

        return new CorsFilter(source);
    }
}