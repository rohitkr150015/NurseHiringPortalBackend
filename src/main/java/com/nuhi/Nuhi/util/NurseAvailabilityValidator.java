package com.nuhi.Nuhi.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nuhi.Nuhi.enums.ProfileStatus;
import com.nuhi.Nuhi.exception.NurseNotAvailableException;
import com.nuhi.Nuhi.model.NurseProfile;
import com.nuhi.Nuhi.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NurseAvailabilityValidator {

    private final BookingRepository bookingRepository;
    private final ObjectMapper objectMapper;


    public void validate(NurseProfile nurse , LocalDateTime start , LocalDateTime end){

        //check approval status
        if(nurse.getStatus() != ProfileStatus.APPROVED){
            throw new NurseNotAvailableException("Nurse profile not approved");
        }

        //checking time conflicts

        if (hasBookingConflict(nurse.getUser().getId() , start , end)){
            throw new NurseNotAvailableException("Nurse not available at required time");
        }
    }

    private boolean hasBookingConflict(Long nurseId, LocalDateTime start, LocalDateTime end) {
        return bookingRepository.existsByNurseIdAndTimeRange(nurseId , start ,end);
    }

    private boolean isWIthinAvailableSlots(String availabilitJson  , LocalDateTime start , LocalDateTime end){

        try{
            Map<String ,List<String>> availability = parseAvailability(availabilitJson);

            String dayOfWeek = start.getDayOfWeek().toString().substring(0,3);

            List <String> availableSlots = availability.getOrDefault(dayOfWeek, Collections.emptyList());
            return availableSlots.stream().anyMatch(slot ->
                    isTimeInSlot(start.toLocalTime(), end.toLocalTime(), slot)
            );
        } catch (JsonProcessingException e) {
            throw new NurseNotAvailableException("Invalid availability data format");
        }
    }

    private Map<String, List<String>> parseAvailability(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, new TypeReference<>() {});
    }

    private boolean isTimeInSlot(LocalTime start, LocalTime end, String slot) {
        String[] times = slot.split("-");
        LocalTime slotStart = LocalTime.parse(times[0]);
        LocalTime slotEnd = LocalTime.parse(times[1]);
        return !start.isBefore(slotStart) && !end.isAfter(slotEnd);
    }
        }


