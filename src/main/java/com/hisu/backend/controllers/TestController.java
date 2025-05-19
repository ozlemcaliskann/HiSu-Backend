package com.hisu.backend.controllers;

import com.hisu.backend.models.ServiceProvider;
import com.hisu.backend.services.ServiceProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/test")
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);
    private final ServiceProviderService serviceProviderService;

    public TestController(ServiceProviderService serviceProviderService) {
        this.serviceProviderService = serviceProviderService;
    }

    @GetMapping("/public")
    public ResponseEntity<String> publicEndpoint() {
        logger.info("Public endpoint accessed");
        return ResponseEntity.ok("Public data - no authentication required");
    }
    
    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        logger.info("Status endpoint accessed");
        return ResponseEntity.ok("{\"status\":\"UP\",\"message\":\"Server is running\"}");
    }
    
    @GetMapping("/public/serviceProviders")
    public ResponseEntity<List<ServiceProvider>> getPublicServiceProviders() throws ExecutionException, InterruptedException {
        logger.info("Public service providers endpoint accessed");
        List<ServiceProvider> providers = serviceProviderService.getAllServiceProviders();
        logger.info("Returning {} public service providers", providers.size());
        return ResponseEntity.ok(providers);
    }
} 