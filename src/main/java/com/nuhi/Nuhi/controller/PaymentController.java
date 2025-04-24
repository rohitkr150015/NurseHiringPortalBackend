package com.nuhi.Nuhi.controller;



import com.nuhi.Nuhi.dto.PaymentRequestDTO;

import com.nuhi.Nuhi.exception.PaymentNotFoundException;
import com.nuhi.Nuhi.exception.UnauthorizedAccessException;
import com.nuhi.Nuhi.model.Booking;
import com.nuhi.Nuhi.model.Payment;
import com.nuhi.Nuhi.model.User;
import com.nuhi.Nuhi.repository.PaymentRepository;
import com.nuhi.Nuhi.security.SecurityUtils;
import com.nuhi.Nuhi.service.BookingService;
import com.nuhi.Nuhi.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

  private final PaymentService paymentService;
  private final BookingService bookingService;
    private final PaymentRepository paymentRepository;
private final SecurityUtils securityUtils;

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Payment> createPayment(@RequestBody @Valid PaymentRequestDTO requestDTO) {

      Booking booking=bookingService.getBookingEnttity(requestDTO.bookingId());

      // validation to ensure the booking belongs to the current user:
        User currentUser = securityUtils.getCurrentUser()
                .orElseThrow(() -> new UnauthorizedAccessException("User not authenticated"));

        if (!booking.getClient().getId().equals(currentUser.getId())) {
            throw new UnauthorizedAccessException("You can only pay for your own bookings");
        }

      Payment payment=paymentService.processPayment(booking, requestDTO.paymentMethodId());
      return ResponseEntity.ok(payment);
  }

  @GetMapping("/{transactionId}")
  @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")

    public ResponseEntity<Payment> getPayment(
            @PathVariable String transactionId
  ){
      return ResponseEntity.ok(paymentRepository.findByTransactionId(transactionId)
              .orElseThrow(()-> new PaymentNotFoundException("Payment not found"))
      );
  }

}
