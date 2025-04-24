package com.nuhi.Nuhi.service;


import com.nuhi.Nuhi.dto.BookingDTO;
import com.nuhi.Nuhi.enums.BookingStatus;
import com.nuhi.Nuhi.enums.PaymentMethod;
import com.nuhi.Nuhi.enums.PaymentStatus;
import com.nuhi.Nuhi.exception.PaymentProcessingException;
import com.nuhi.Nuhi.model.Booking;
import com.nuhi.Nuhi.model.Payment;
import com.nuhi.Nuhi.repository.BookingRepository;
import com.nuhi.Nuhi.repository.PaymentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final StripService stripService;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;


    public Payment processPayment(Booking booking, String paymentMethodId) {
        try {
            //create and confirm payment intent

            PaymentIntent paymentIntent = stripService.createPaymentIntent(
                    booking.getTotalCost(), paymentMethodId
            );

            //save payment Record

            Payment payment = Payment.builder()
                    .booking(booking)
                    .amount(booking.getTotalCost())
                    .paymentMethod(PaymentMethod.CARD)
                    .paymentStatus(mapStripeStatus(paymentIntent.getStatus()))
                    .transactionId(paymentIntent.getId())
                    .paidAt(LocalDateTime.now())
                    .build();

            paymentRepository.save(payment);


            // update Booking status

            if (payment.getPaymentStatus() == PaymentStatus.SUCCEEDED) {
                booking.setStatus(BookingStatus.CONFIRMED);
            }

            return payment;
        } catch (StripeException e) {
            throw new PaymentProcessingException("Payment Failed" + e.getMessage());

        }
    }

    private PaymentStatus mapStripeStatus(String stripeStatus) {
        return switch (stripeStatus) {
            case "succeeded" -> PaymentStatus.SUCCEEDED;
            case "required_payment_method", "requires_confirmation" -> PaymentStatus.PENDING;
            case "requires_action" -> PaymentStatus.REQUIRES_ACTION;
            case "processing" -> PaymentStatus.PROCESSING;
            case "cancelled" -> PaymentStatus.CANCELED;
            default -> PaymentStatus.FAILED;
        };
    }


}


