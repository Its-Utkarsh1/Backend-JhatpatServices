package com.example.demo.Dto.Request;

import com.example.demo.Model.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ServiceRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Category is required")
    private Category category;

    @Positive(message = "Price must be positive")
    @NotNull(message = "Price is required")
    private BigDecimal price;

    private String imageUrl;
}
