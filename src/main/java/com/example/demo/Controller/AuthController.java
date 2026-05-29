package com.example.demo.Controller;

import com.example.demo.Dto.Request.LoginRequest;
import com.example.demo.Dto.Request.OtpRequest;
import com.example.demo.Dto.Request.RegisterRequest;
import com.example.demo.Dto.Response.AuthResponse;
import com.example.demo.Service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest req) {
        String message = authService.register(req);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(
            @Valid @RequestBody OtpRequest req) {

        String message = authService.verifyotp(req.getEmail(), req.getOtp());
        return ResponseEntity.ok(message);
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<String> resendOtp(
            @RequestParam String email) {

        String message = authService.resendOtp(email);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse>login(
            @Valid @RequestBody LoginRequest req) {

        AuthResponse authResponse = authService.login(req);
        return ResponseEntity.ok(authResponse);
    }

}
