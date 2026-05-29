package com.example.demo.Service;

import com.example.demo.Dto.Request.BookingRequest;
import com.example.demo.Dto.Response.BookingResponse;
import com.example.demo.Model.Booking;
import com.example.demo.Model.BookingStatus;
import com.example.demo.Model.User;
import com.example.demo.Model.ServiceEntity;
import com.example.demo.Repository.BookingRepository;
import com.example.demo.Repository.ServiceRepository;
import com.example.demo.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;


    @Transactional
    public BookingResponse create(BookingRequest req, String customerEmail) {
        User customer = findUser(customerEmail);

        ServiceEntity service = serviceRepository.findById(req.getServiceId()).orElseThrow(() -> new IllegalArgumentException("Service not found"));


        Booking booking = Booking.builder()
                .customer(customer)
                .service(service)
                .scheduledAt(req.getScheduledAt())
                .address(req.getAddress())
                .notes(req.getNotes())
                .status(BookingStatus.PENDING)
                .build();

        Booking saved = bookingRepository.save(booking);

        // Send confirmation email (async — non-blocking)
        emailService.sendBookingConfirmation(
                customerEmail,
                customer.getFullName(),
                service.getTitle(),
                req.getScheduledAt().toString()
        );

        log.info("Booking created id={} by {}", saved.getId(), customerEmail);
        return toResponse(saved);
    }

    // ── CUSTOMER BOOKINGS ─────────────────────────────────────────────────────

    public List<BookingResponse> getCustomerBookings(String customerEmail) {
        User customer = findUser(customerEmail);
        return bookingRepository.findByCustomerOrderByCreatedAtDesc(customer)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ── PROVIDER BOOKINGS ─────────────────────────────────────────────────────

    public List<BookingResponse> getProviderBookings(String providerEmail) {
        User provider = findUser(providerEmail);
        return bookingRepository.findByService_ProviderOrderByCreatedAtDesc(provider)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ── UPDATE STATUS (provider) ──────────────────────────────────────────────

    @Transactional
    public BookingResponse updateStatus(Long bookingId,
                                        BookingStatus status,
                                        String providerEmail) {
        Booking booking = findBooking(bookingId);

        if (!booking.getService().getProvider().getEmail().equals(providerEmail)) {
            throw new IllegalArgumentException("Not authorized to update this booking");
        }

        booking.setStatus(status);
        return toResponse(bookingRepository.save(booking));
    }

    // ── CANCEL (customer) ─────────────────────────────────────────────────────

    @Transactional
    public BookingResponse cancel(Long bookingId, String customerEmail) {
        Booking booking = findBooking(bookingId);

        if (!booking.getCustomer().getEmail().equals(customerEmail)) {
            throw new IllegalArgumentException("Not authorized to cancel this booking");
        }
        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new IllegalArgumentException("Cannot cancel a completed booking");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        return toResponse(bookingRepository.save(booking));
    }

    // ── HELPERS ───────────────────────────────────────────────────────────────

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private Booking findBooking(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
    }

    // Entity → Response DTO
    private BookingResponse toResponse(Booking b) {
        return BookingResponse.builder()
                .id(b.getId())
                .customerName(b.getCustomer().getFullName())
                .customerEmail(b.getCustomer().getEmail())
                .serviceTitle(b.getService().getTitle())
                .providerName(b.getService().getProvider().getFullName())
                .scheduledAt(b.getScheduledAt())
                .address(b.getAddress())
                .notes(b.getNotes())
                .status(b.getStatus())
                .createdAt(b.getCreatedAt())
                .build();
    }

}
