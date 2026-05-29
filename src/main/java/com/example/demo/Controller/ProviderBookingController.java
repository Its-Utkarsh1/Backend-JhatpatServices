package com.example.demo.Controller;

import com.example.demo.Dto.Response.BookingResponse;
import com.example.demo.Model.BookingStatus;
import com.example.demo.Service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/provider/bookings")
@RequiredArgsConstructor
public class ProviderBookingController {

    private final BookingService bookingService;


    // ── PROVIDER ──────────────────────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<List<BookingResponse>> providerBookings(
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(bookingService.getProviderBookings(userDetails.getUsername()));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<BookingResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam BookingStatus status,
            @AuthenticationPrincipal UserDetails userDetails) {

        BookingResponse booking = bookingService.updateStatus(
                id, status, userDetails.getUsername());
        return ResponseEntity.ok(booking);
    }
}
