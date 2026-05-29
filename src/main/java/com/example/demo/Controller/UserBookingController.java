package com.example.demo.Controller;

import com.example.demo.Dto.Request.BookingRequest;
import com.example.demo.Dto.Response.BookingResponse;
import com.example.demo.Service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class UserBookingController {

    private final BookingService bookingService;

    // ── USER ──────────────────────────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            @Valid @RequestBody BookingRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {

        BookingResponse booking = bookingService.create(req, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }

    @GetMapping("/myBookings")
    public ResponseEntity<List<BookingResponse>> myBookings(
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok( bookingService.getCustomerBookings(userDetails.getUsername()));
    }

    @PatchMapping("{id}/cancel")
    public ResponseEntity<BookingResponse> cancelBooking(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        BookingResponse booking = bookingService.cancel(id, userDetails.getUsername());
        return ResponseEntity.ok(booking);
    }



}
