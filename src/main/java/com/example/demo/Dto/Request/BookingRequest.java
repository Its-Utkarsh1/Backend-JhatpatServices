package com.example.demo.Dto.Request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingRequest {

    @NotNull(message = "Service ID is required")
    private Long serviceId;

    @NotNull(message = "Scheduled time is required")
    @Future(message = "Scheduled time must be in the future")
    private LocalDateTime scheduledAt;

    private String address;

    private String notes;
}
