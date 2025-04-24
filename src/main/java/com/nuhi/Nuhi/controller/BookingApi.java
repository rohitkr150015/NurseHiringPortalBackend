package com.nuhi.Nuhi.controller;

import com.nuhi.Nuhi.dto.BookingDTO;
import com.nuhi.Nuhi.dto.BookingRequestDTO;
import com.nuhi.Nuhi.dto.BookingResponseDTO;
import com.nuhi.Nuhi.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingApi {

    private final BookingService bookingService;

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<BookingDTO> createBooking(
            @RequestBody @Valid BookingRequestDTO request
    ) {
        return ResponseEntity.ok(bookingService.createBooking(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('CLIENT', 'NURSE')")
    public ResponseEntity<List<BookingDTO>> getUserBookings() {
        return ResponseEntity.ok(bookingService.getUserBookings());
    }
}