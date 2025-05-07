package com.hisu.backend.controllers;

import com.hisu.backend.models.ServiceProvider;
import com.hisu.backend.services.ServiceProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class ServiceProviderController {
    private final ServiceProviderService serviceProviderService;
    private static final Logger logger = LoggerFactory.getLogger(ServiceProviderController.class);

    public ServiceProviderController(ServiceProviderService serviceProviderService) {
        this.serviceProviderService = serviceProviderService;
    }

    @GetMapping("/serviceProviders")
    public ResponseEntity<List<ServiceProvider>> getAllServiceProviders() throws ExecutionException, InterruptedException {
        logger.info("GET /api/serviceProviders called");
        List<ServiceProvider> providers = serviceProviderService.getAllServiceProviders();
        logger.info("Returning {} service providers", providers.size());
        return ResponseEntity.ok(providers);
    }

    @GetMapping("/serviceProviders/{id}")
    public ResponseEntity<ServiceProvider> getServiceProviderById(@PathVariable String id) throws ExecutionException, InterruptedException {
        logger.info("GET /api/serviceProviders/{} called", id);
        ServiceProvider provider = serviceProviderService.getServiceProviderById(id);
        return ResponseEntity.ok(provider);
    }

    @PostMapping("/serviceProviders")
    public ResponseEntity<String> createServiceProvider(@RequestBody ServiceProvider provider) throws ExecutionException, InterruptedException {
        logger.info("POST /api/serviceProviders called");
        String id = serviceProviderService.createServiceProvider(provider);
        return ResponseEntity.ok(id);
    }

    @PatchMapping("/serviceProviders/{id}")
    public ResponseEntity<Void> updateServiceProvider(@PathVariable String id, @RequestBody Map<String, Object> updates) throws ExecutionException, InterruptedException {
        logger.info("PATCH /api/serviceProviders/{} called", id);
        serviceProviderService.updateServiceProvider(id, updates);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/serviceProviders/{id}")
    public ResponseEntity<Void> deleteServiceProvider(@PathVariable String id) throws ExecutionException, InterruptedException {
        logger.info("DELETE /api/serviceProviders/{} called", id);
        serviceProviderService.deleteServiceProvider(id);
        return ResponseEntity.ok().build();
    }
} 