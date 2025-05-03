package com.hisu.backend.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class FirebaseAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String idToken = request.getHeader("Authorization");

        // If no token is provided, continue without authentication
        if (idToken == null || idToken.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Verify the token directly without expecting "Bearer " prefix
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String email = decodedToken.getEmail();

            if (email == null || !email.endsWith("@sabanciuniv.edu")) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Only @sabanciuniv.edu email addresses are allowed");
                return;
            }

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    decodedToken.getUid(),
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority("PRIVATE"))
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Add the Firebase user ID to the request attributes
            request.setAttribute("firebaseUid", decodedToken.getUid());

        } catch (FirebaseAuthException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Invalid authentication token");
            return;
        }

        filterChain.doFilter(request, response);
    }
}