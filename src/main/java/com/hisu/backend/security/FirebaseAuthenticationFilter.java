package com.hisu.backend.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class FirebaseAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Skip authentication for OPTIONS requests (CORS preflight)
        if (request.getMethod().equals("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // Allow public endpoints without authentication
        if (request.getRequestURI().startsWith("/auth/") || 
            request.getRequestURI().startsWith("/api/test/public")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        String authHeader = request.getHeader("Authorization");

        // If no token is provided, return 401 Unauthorized
        if (authHeader == null || authHeader.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authentication required");
            return;
        }

        try {
            // Extract token (with or without "Bearer " prefix)
            String idToken = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
            
            // Verify the token
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String uid = decodedToken.getUid();
            String email = decodedToken.getEmail();

            // Check if email is from Sabanci University
            if (email == null || !email.endsWith("@sabanciuniv.edu")) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Only @sabanciuniv.edu email addresses are allowed");
                return;
            }

            // Create the authentication with PRIVATE role
            List<SimpleGrantedAuthority> authorities = Arrays.asList(
                new SimpleGrantedAuthority("ROLE_PRIVATE")
            );
            
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    uid,
                    null,
                    authorities
            );
            
            // Set authentication in context
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Add Firebase user ID to request attributes
            request.setAttribute("firebaseUid", uid);
            
            // Print a clear log message with PRIVATE role
            logger.info("User authenticated successfully: UID={} Role=ROLE_PRIVATE", uid);

        } catch (FirebaseAuthException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Invalid authentication token: " + e.getMessage());
            return;
        }

        // Continue with filter chain
        filterChain.doFilter(request, response);
    }
}