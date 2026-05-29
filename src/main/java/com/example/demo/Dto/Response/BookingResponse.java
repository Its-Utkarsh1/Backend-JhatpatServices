package com.example.demo.Dto.Response;

import com.example.demo.Model.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private Long id;
    private String customerName;
    private String customerEmail;
    private String serviceTitle;
    private String providerName;
    private LocalDateTime scheduledAt;
    private String address;
    private String notes;
    private BookingStatus status;
    private LocalDateTime createdAt;
}

