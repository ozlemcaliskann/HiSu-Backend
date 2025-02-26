package com.hisu.backend.config;

import com.hisu.backend.security.FirebaseAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final FirebaseAuthenticationFilter firebaseAuthenticationFilter;

    public SecurityConfig(FirebaseAuthenticationFilter firebaseAuthenticationFilter) {
        this.firebaseAuthenticationFilter = firebaseAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // Allow public access to authentication endpoints
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Restrict /admin routes to ADMIN
                        .requestMatchers("/private/**").hasAnyRole("PRIVATE", "ADMIN") // Private users & admins can access
                        .anyRequest().authenticated())
                .addFilterBefore(firebaseAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
