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
    
    // Public yollar listesi - bu yollara token gerekmeden erişilebilir
    private static final List<String> PUBLIC_PATHS = Arrays.asList(
        "/auth/",
        "/api/test/",
        "/api/serviceProviders",
        "/api/clubs",
        "/api/activities"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Skip authentication for OPTIONS requests (CORS preflight)
        if (request.getMethod().equals("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // Tüm GET isteklerine izin ver
        if (request.getMethod().equals("GET")) {
            logger.info("Anonymous GET access: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }
        
        // Public endpoint'ler için doğrulama atla
        String requestUri = request.getRequestURI();
        for (String publicPath : PUBLIC_PATHS) {
            if (requestUri.startsWith(publicPath)) {
                logger.info("Public endpoint access: {}", requestUri);
                filterChain.doFilter(request, response);
                return;
            }
        }
        
        String authHeader = request.getHeader("Authorization");

        // Token yoksa 401 Unauthorized dön
        if (authHeader == null || authHeader.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authentication required");
            return;
        }

        try {
            // Token'ı çıkar ("Bearer " öneki var mı yok mu)
            String idToken = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
            
            // Token'ı doğrula
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String uid = decodedToken.getUid();
            String email = decodedToken.getEmail();

            // Sabancı Üniversitesi mail kontrolü
            if (email == null || !email.endsWith("@sabanciuniv.edu")) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Only @sabanciuniv.edu email addresses are allowed");
                return;
            }

            // PRIVATE rolü ile authentication oluştur
            List<SimpleGrantedAuthority> authorities = Arrays.asList(
                new SimpleGrantedAuthority("ROLE_PRIVATE")
            );
            
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    uid,
                    null,
                    authorities
            );
            
            // Authentication'ı context'e set et
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Firebase user ID'sini request özniteliklerine ekle
            request.setAttribute("firebaseUid", uid);
            
            // Log mesajı
            logger.info("User authenticated successfully: UID={} Role=ROLE_PRIVATE", uid);

        } catch (FirebaseAuthException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Invalid authentication token: " + e.getMessage());
            return;
        }

        // Filter zinciriyle devam et
        filterChain.doFilter(request, response);
    }
}