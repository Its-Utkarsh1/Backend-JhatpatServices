package com.example.demo.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OtpRequest {

    private String email;
    private String otp;
    private LocalDateTime createdAt;

}
