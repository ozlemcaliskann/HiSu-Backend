package com.hisu.backend.services;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.hisu.backend.models.ServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class ServiceProviderService {
    private static final Logger logger = LoggerFactory.getLogger(ServiceProviderService.class);
    private final Firestore firestore;
    private static final String COLLECTION_NAME = "serviceProviders";

    public ServiceProviderService(Firestore firestore) {
        this.firestore = firestore;
    }

    public List<ServiceProvider> getAllServiceProviders() throws ExecutionException, InterruptedException {
        try {
            logger.debug("Getting all service providers");
            QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME).get().get();
            
            if (querySnapshot.isEmpty()) {
                logger.info("No service providers found, returning demo data");
                return createMockServiceProviders();
            }
            
            List<ServiceProvider> providers = querySnapshot.getDocuments().stream()
                    .map(doc -> doc.toObject(ServiceProvider.class))
                    .collect(Collectors.toList());
            
            logger.info("Returning {} service providers", providers.size());
            return providers;
        } catch (Exception e) {
            logger.error("Error getting service providers", e);
            // Hata durumunda demo veri döndür
            return createMockServiceProviders();
        }
    }
    
    public ServiceProvider getServiceProviderById(String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = firestore.collection(COLLECTION_NAME).document(id).get().get();
        if (document.exists()) {
            return document.toObject(ServiceProvider.class);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Service provider not found");
    }
    
    public String createServiceProvider(ServiceProvider provider) throws ExecutionException, InterruptedException {
        if (provider.getId() == null || provider.getId().isEmpty()) {
            provider.setId(UUID.randomUUID().toString());
        }
        
        if (provider.getCreatedAt() == null) {
            provider.setCreatedAt(Timestamp.now());
        }
        
        firestore.collection(COLLECTION_NAME).document(provider.getId()).set(provider).get();
        return provider.getId();
    }
    
    public void updateServiceProvider(String id, Map<String, Object> updates) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = firestore.collection(COLLECTION_NAME).document(id).get().get();
        if (!document.exists()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Service provider not found");
        }
        
        firestore.collection(COLLECTION_NAME).document(id).update(updates).get();
    }
    
    public void deleteServiceProvider(String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = firestore.collection(COLLECTION_NAME).document(id).get().get();
        if (!document.exists()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Service provider not found");
        }
        
        firestore.collection(COLLECTION_NAME).document(id).delete().get();
    }
    
    // Demo veri oluşturma metodu - veritabanında veri yoksa kullanılır
    private List<ServiceProvider> createMockServiceProviders() {
        List<ServiceProvider> providers = new ArrayList<>();
        
        providers.add(new ServiceProvider("1", "Sabancı IT Servisi", 
                "Üniversite kampüsündeki IT sorunları için destek sağlar", "Teknoloji"));
        
        providers.add(new ServiceProvider("2", "Kampüs Sağlık Merkezi", 
                "Öğrenciler ve akademisyenler için sağlık hizmetleri", "Sağlık"));
        
        providers.add(new ServiceProvider("3", "SU Kütüphane", 
                "Sabancı Üniversitesi bilgi ve yayınlar merkezi", "Eğitim"));
        
        providers.add(new ServiceProvider("4", "Kampüs Spor Tesisleri", 
                "Fitness ve spor aktiviteleri için tesisler", "Spor"));
        
        return providers;
    }
} 