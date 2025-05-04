package com.hisu.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Allow credentials
        config.setAllowCredentials(true);

        // Allow origins for different environments
        List<String> allowedOrigins = Arrays.asList(
                "http://localhost:19006",      // Expo web
                "http://localhost:19000",      // Expo development
                "http://localhost:19001",      // Expo development alternate
                "http://localhost:19002",      // Expo development alternate
                "http://10.0.2.2:19006",       // Android emulator
                "http://10.0.2.2:8080",        // Android emulator backend
                "http://localhost:8080",       // Local backend
                "capacitor://localhost",       // Capacitor
                "ionic://localhost",           // Ionic
                "http://localhost",            // General localhost
                "http://localhost:3000"        // React development
        );

        config.setAllowedOriginPatterns(Arrays.asList(
                "http://localhost:[*]",
                "http://10.0.2.2:[*]",
                "exp://[*]",                   // Expo development URLs
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