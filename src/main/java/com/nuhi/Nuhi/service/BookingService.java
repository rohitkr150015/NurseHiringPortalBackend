package com.nuhi.Nuhi.service;

import com.nuhi.Nuhi.dto.BookingDTO;
import com.nuhi.Nuhi.dto.BookingRequestDTO;

import com.nuhi.Nuhi.enums.BookingStatus;
import com.nuhi.Nuhi.exception.BookingNotFoundException;
import com.nuhi.Nuhi.exception.NurseNotFoundException;
import com.nuhi.Nuhi.exception.UnauthorizedAccessException;
import com.nuhi.Nuhi.model.Booking;
import com.nuhi.Nuhi.model.NurseProfile;
import com.nuhi.Nuhi.model.User;
import com.nuhi.Nuhi.repository.BookingRepository;
import com.nuhi.Nuhi.repository.NurseProfileRepository;
import com.nuhi.Nuhi.repository.ServiceRepository;
import com.nuhi.Nuhi.repository.UserRepository;
import com.nuhi.Nuhi.security.SecurityUtils;
import com.nuhi.Nuhi.util.NurseAvailabilityValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.nuhi.Nuhi.exception.ServiceNotFoundException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {




private final BookingRepository bookingRepo;
private final NurseProfileRepository nurseRepo;
private final ServiceRepository serviceRepo;
private final UserRepository userRepo;
private final NurseAvailabilityValidator availabilityValidator;
    private final SecurityUtils securityUtils;


    @Transactional
    public BookingDTO createBooking(BookingRequestDTO request) {
        // 1. Validate nurse availability
        NurseProfile nurse = nurseRepo.findById(request.nurseId())
                .orElseThrow(() -> new NurseNotFoundException("Nurse not found"));

        availabilityValidator.validate(nurse, request.startTime(), request.endTime());

        // 2. Calculate cost
        com.nuhi.Nuhi.model.Service service = serviceRepo.findById(request.serviceId())
                .orElseThrow(() -> new ServiceNotFoundException("Service not found"));

        BigDecimal totalCost = calculateCost(
                new BigDecimal(service.getBasePrice()),
                request.startTime(),
                request.endTime()
        );

        // 3. Create booking
        User client = securityUtils.getCurrentUser()
                .orElseThrow(() -> new UnauthorizedAccessException("User not authenticated"));

        Booking booking = Booking.builder()
                .client(client)
                .nurse(nurse.getUser())
                .service(service)
                .startTime(request.startTime())
                .endTime(request.endTime())
                .totalCost(totalCost)
                .status(BookingStatus.CONFIRMED)
                .build();

        Booking savedBooking = bookingRepo.save(booking);

        return toDTO(savedBooking);
    }
    private BookingDTO toDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setBookingId(booking.getId());
        dto.setClientId(booking.getClient().getId());
        dto.setNurseId(booking.getNurse().getId());
        dto.setServiceId(booking.getService().getServiceId());
        dto.setStartTime(booking.getStartTime());
        dto.setEndTime(booking.getEndTime());
        dto.setTotalCost(booking.getTotalCost());
        dto.setStatus(booking.getStatus());
        dto.setClientName(booking.getClient().getFirstName() + " " + booking.getClient().getLastName());
        dto.setNurseName(booking.getNurse().getFirstName() + " " + booking.getNurse().getLastName());
        dto.setServiceName(booking.getService().getName());
        return dto;
    }

    private BigDecimal calculateCost(BigDecimal basePrice, LocalDateTime start, LocalDateTime end) {
        long hours = Duration.between(start, end).toHours();
        return basePrice.multiply(BigDecimal.valueOf(hours));
    }

    public BookingDTO getBookingById(Long id) {
        return bookingRepo.findById(id)
                .map(this::toDTO) // Use the local toDTO method
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));
    }

    public List<BookingDTO> getUserBookings() {
        User user = securityUtils.getCurrentUser()
                .orElseThrow(() -> new UnauthorizedAccessException("User not authenticated"));

        return bookingRepo.findByClientId(user.getId()).stream()
                .map(this::toDTO) // Use the local toDTO method
                .toList();
    }

    public Booking getBookingEnttity(Long id) {
        return bookingRepo.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));
    }

}
