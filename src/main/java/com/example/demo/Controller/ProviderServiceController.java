package com.example.demo.Controller;

import com.example.demo.Dto.Request.ServiceRequest;
import com.example.demo.Dto.Response.ServiceResponse;
import com.example.demo.Service.ServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/provider/services")
@RequiredArgsConstructor
public class ProviderServiceController {

    private final ServiceService serviceService;

    // ── PROVIDER ──────────────────────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<List<ServiceResponse>> myServices(
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(serviceService.getProviderServices(userDetails.getUsername()));
    }

    @PostMapping
    public ResponseEntity<ServiceResponse> createService(
            @Valid @RequestBody ServiceRequest req,
            @AuthenticationPrincipal UserDetails userDetails) throws IOException {

        ServiceResponse created = serviceService.create(req, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse> updateService(
            @PathVariable Long id,
            @Valid @RequestPart("data") ServiceRequest req,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @AuthenticationPrincipal UserDetails userDetails) throws IOException {

        ServiceResponse updated = serviceService.update(id, req, image, userDetails.getUsername());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public void deleteService(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        serviceService.delete(id, userDetails.getUsername());
    }

}


