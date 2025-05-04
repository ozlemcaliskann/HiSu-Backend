package com.hisu.backend.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class FirebaseAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        System.out.println("CUSTOM DEBUG: Filter processing: " + request.getRequestURI());
        System.out.println("CUSTOM DEBUG: Request method: " + request.getMethod());
        
        // Skip authentication for these paths
        if (request.getMethod().equals("OPTIONS") ||
            request.getRequestURI().equals("/") ||          // Allow root path
            request.getRequestURI().equals("/favicon.ico") ||  // Allow favicon
            request.getRequestURI().startsWith("/api/test") ||
            request.getRequestURI().startsWith("/auth")) {
            System.out.println("CUSTOM DEBUG: Skipping authentication for: " + request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        System.out.println("CUSTOM DEBUG: Auth header: " + (authHeader != null ? "present" : "missing"));

        if (authHeader == null || authHeader.isEmpty()) {
            System.out.println("CUSTOM DEBUG: No Authorization header found");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authentication required");
            return;
        }

        try {
            String idToken = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
            System.out.println("CUSTOM DEBUG: Token starting with: " + 
                (idToken.length() > 20 ? idToken.substring(0, 20) + "..." : idToken));
            
            // Debug token format
            String[] tokenParts = idToken.split("\\.");
            if (tokenParts.length != 3) {
                System.out.println("CUSTOM DEBUG: Invalid token format - not a valid JWT");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Invalid token format");
                return;
            }
            
            System.out.println("CUSTOM DEBUG: Attempting to verify token with Firebase");
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String uid = decodedToken.getUid();
            String email = decodedToken.getEmail();

            System.out.println("CUSTOM DEBUG: Token verified for user: " + email + " (uid: " + uid + ")");

            // Check if email has correct domain
            if (email == null || !email.endsWith("@sabanciuniv.edu")) {
                System.out.println("CUSTOM DEBUG: Email domain not allowed: " + email);
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Only @sabanciuniv.edu email addresses are allowed");
                return;
            }

            // Grant authority and set authentication
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("PRIVATE");
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(uid, null, Collections.singletonList(authority));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            request.setAttribute("firebaseUid", uid);

            System.out.println("CUSTOM DEBUG: Authentication successful, proceeding with request");

        } catch (FirebaseAuthException e) {
            System.out.println("CUSTOM DEBUG: Firebase authentication failed: " + e.getMessage());
            System.out.println("CUSTOM DEBUG: Error code: " + e.getErrorCode());
            e.printStackTrace();
            
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Invalid authentication token: " + e.getMessage());
            return;
        } catch (Exception e) {
            System.out.println("CUSTOM DEBUG: Unexpected error: " + e.getMessage());
            e.printStackTrace();
            
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getWriter().write("Server error during authentication");
            return;
        }

        filterChain.doFilter(request, response);
    }
}