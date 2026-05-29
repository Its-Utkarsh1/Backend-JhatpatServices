package com.example.demo.Dto.Response;


import com.example.demo.Model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResponse {
    private Long id;
    private String title;
    private String description;
    private Category category;
    private BigDecimal price;
    private String serviceImage;
    private boolean isAvailable;
    private String providerName;
    private String providerEmail;
    private LocalDateTime createdAt;
}

