package com.example.demo.Service;


import com.example.demo.Dto.Request.ServiceRequest;
import com.example.demo.Dto.Response.ServiceResponse;
import com.example.demo.Model.Category;
import com.example.demo.Model.ServiceEntity;
import com.example.demo.Model.User;
import com.example.demo.Repository.ServiceRepository;
import com.example.demo.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;

    private static final String UPLOAD_DIR = "uploads/services/";
    private final ModelMapper modelMapper;

    // ── GET ALL (cached in Redis) ─────────────────────────────────────────────

    @Cacheable(value = "services", key = "'all'")
    public List<ServiceResponse> getAllAvailable() {
        log.info("DB hit: getAllAvailable");
        return serviceRepository.findByIsAvailableTrue()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Cacheable(value = "services", key = "#category.name()")
    public List<ServiceResponse> getByCategory(Category category) {

        return serviceRepository
                .findByCategoryAndIsAvailableTrue(category)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Cacheable(value = "service", key = "#id")
    public ServiceResponse getById(Long id) {
        return toResponse(serviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service not found")));
    }

    public List<ServiceResponse> search(String keyword) {
        return serviceRepository.searchByTitle(keyword)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ── PROVIDER SERVICES ─────────────────────────────────────────────────────

    public List<ServiceResponse> getProviderServices(String providerEmail) {
        User provider = findUser(providerEmail);
        return serviceRepository.findByProvider(provider)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ── CREATE ────────────────────────────────────────────────────────────────

    @Transactional
    @CacheEvict(value = "services", allEntries = true)
    public ServiceResponse create(ServiceRequest req,String providerEmail) throws IOException {
        User provider = findUser(providerEmail);

        ServiceEntity service = ServiceEntity.builder()
                .title(req.getTitle())
                .serviceImage(req.getImageUrl())
                .description(req.getDescription())
                .category(req.getCategory())
                .price(req.getPrice())
                .provider(provider)
                .build();

        return toResponse(serviceRepository.save(service));
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────

    @Transactional
    @CacheEvict(value = {"services", "service"}, allEntries = true)
    public ServiceResponse update(Long id, ServiceRequest req,
                                  MultipartFile image, String providerEmail) throws IOException {
        ServiceEntity service = serviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));

        if (!service.getProvider().getEmail().equals(providerEmail)) {
            throw new AccessDeniedException("Not authorized to edit this service");
        }

        service.setTitle(req.getTitle());
        service.setDescription(req.getDescription());
        service.setCategory(req.getCategory());
        service.setPrice(req.getPrice());

        if (image != null && !image.isEmpty()) {
            service.setServiceImage(saveFile(image));
        }

        return toResponse(serviceRepository.save(service));
    }

    // ── DELETE ────────────────────────────────────────────────────────────────

    @Transactional
    @CacheEvict(value = {"services", "service"}, allEntries = true)
    public void delete(Long id, String providerEmail) {
        ServiceEntity service = serviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));

        if (!service.getProvider().getEmail().equals(providerEmail)) {
            throw new AccessDeniedException("Not authorized to delete this service");
        }

        serviceRepository.delete(service);
    }

    // ── HELPERS ───────────────────────────────────────────────────────────────

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) return null;
        Files.createDirectories(Paths.get(UPLOAD_DIR));
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = Paths.get(UPLOAD_DIR + filename);
        Files.copy(file.getInputStream(), path);
        return UPLOAD_DIR + filename;
    }

    // Entity → Response DTO
    public ServiceResponse toResponse(ServiceEntity s) {
        return ServiceResponse.builder()
                .id(s.getId())
                .title(s.getTitle())
                .description(s.getDescription())
                .category(s.getCategory())
                .price(s.getPrice())
                .serviceImage(s.getServiceImage())
                .isAvailable(s.isAvailable())
                .providerName(s.getProvider().getFullName())
                .providerEmail(s.getProvider().getEmail())
                .createdAt(s.getCreatedAt())
                .build();
    }
}
