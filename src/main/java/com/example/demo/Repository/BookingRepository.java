package com.example.demo.Repository;

import com.example.demo.Model.Booking;
import com.example.demo.Model.BookingStatus;
import com.example.demo.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Customer's bookings
    List<Booking> findByCustomerOrderByCreatedAtDesc(User customer);

    // Bookings for a provider's services
    List<Booking> findByService_ProviderOrderByCreatedAtDesc(User provider);

    // Bookings by status
    List<Booking> findByStatus(BookingStatus status);
}
