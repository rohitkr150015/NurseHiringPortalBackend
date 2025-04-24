package com.nuhi.Nuhi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nuhi.Nuhi.dto.NurseProfileDTO;
import com.nuhi.Nuhi.enums.ProfileStatus;
import com.nuhi.Nuhi.enums.Role;
import com.nuhi.Nuhi.exception.NurseNotFoundException;
import com.nuhi.Nuhi.exception.UnauthorizedAccessException;
import com.nuhi.Nuhi.mapper.NurseProfileMapper;
import com.nuhi.Nuhi.model.NurseProfile;
import com.nuhi.Nuhi.model.User;
import com.nuhi.Nuhi.repository.NurseProfileRepository;
import com.nuhi.Nuhi.repository.UserRepository;
import com.nuhi.Nuhi.security.SecurityUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NurseService {

    private final NurseProfileRepository nurseProfileRepository;
    private final UserRepository userRepository;
    private final NurseProfileMapper nurseProfileMapper;
    private final SecurityUtils securityUtils;
    private final EntityManager entityManager;
    private final ObjectMapper objectMapper;

    @Transactional(rollbackFor = Exception.class)
    public NurseProfileDTO createProfile(NurseProfileDTO dto) {
        // Validate license number
        String cleanLicense = validateAndCleanLicense(dto.getLicenseNumber());

        // Validate JSON format if availability is provided
        if (dto.getAvailability() != null) {
            validateJsonFormat(dto.getAvailability());
        }

        User currentUser = getAuthenticatedNurse();

        NurseProfile profile = nurseProfileMapper.toEntity(dto);
        profile.setLicenseNumber(cleanLicense);
        profile.setUser(currentUser);
        profile.setNurseId(currentUser.getId());
        profile.setStatus(ProfileStatus.PENDING);

        return nurseProfileMapper.toDto(nurseProfileRepository.save(profile));
    }



    @Retryable(value = {
            ObjectOptimisticLockingFailureException.class,
            OptimisticLockException.class
    }, maxAttempts = 3, backoff = @Backoff(delay = 300))
    @Transactional
    public NurseProfileDTO updateProfile(NurseProfileDTO dto) {
        try {
            User currentUser = getAuthenticatedNurse();
            NurseProfile existingProfile = nurseProfileRepository.findById(dto.getNurseId())
                    .orElseThrow(() -> new NurseNotFoundException("Profile not found"));

            validateProfileOwnership(existingProfile, currentUser);

            // Validate JSON if availability is being updated
            if (dto.getAvailability() != null) {
                validateJsonFormat(dto.getAvailability());
            }

            // Update only allowed fields
            nurseProfileMapper.updateEntityFromDto(dto, existingProfile);

            // Explicit save to ensure version increment
            NurseProfile updated = nurseProfileRepository.save(existingProfile);
            return nurseProfileMapper.toDto(updated);

        } catch (OptimisticLockException e) {
            throw new ConcurrentModificationException("Profile was modified by another process. Please refresh and try again.");
        }
    }

    private void validateProfileOwnership(NurseProfile profile, User currentUser) {
        if (!profile.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedAccessException("You can only update your own profile");
        }
    }



    public List<NurseProfileDTO> findApprovedNurses(String location, String specialization) {
        return nurseProfileRepository.findByStatusAndFilters(ProfileStatus.APPROVED, location, specialization)
                .stream()
                .map(nurseProfileMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void approveProfile(Long nurseId, ProfileStatus status) {
        User admin = getAuthenticatedAdmin();
        NurseProfile profile = nurseProfileRepository.findById(nurseId)
                .orElseThrow(() -> new NurseNotFoundException("Profile Not Found"));
        profile.setStatus(ProfileStatus.APPROVED);
    }

    // Helper methods
    private String validateAndCleanLicense(String license) {
        if (license == null || license.isBlank()) {
            throw new IllegalArgumentException("License number is required");
        }

        String cleanLicense = license.trim().toUpperCase();
        if (!cleanLicense.matches("^[A-Z0-9-]{10,20}$")) {
            throw new IllegalArgumentException(
                    "License number must be 10-20 characters (A-Z, 0-9, hyphens)");
        }
        return cleanLicense;
    }

    private void validateJsonFormat(String json) {
        try {
            objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid JSON format for availability");
        }
    }

    private User getAuthenticatedNurse() {
        User user = securityUtils.getCurrentUser()
                .orElseThrow(() -> new UnauthorizedAccessException("User not authenticated"));
        if (user.getRole() != Role.NURSE) {
            throw new UnauthorizedAccessException("Only nurses can perform this action");
        }
        return user;
    }

    private User getAuthenticatedAdmin() {
        User user = securityUtils.getCurrentUser()
                .orElseThrow(() -> new UnauthorizedAccessException("User not authenticated"));
        if (user.getRole() != Role.ADMIN) {
            throw new UnauthorizedAccessException("Only admins can perform this action");
        }
        return user;
    }

    private NurseProfile getExistingProfile(Long nurseId, User currentUser) {
        NurseProfile profile = nurseProfileRepository.findById(nurseId)
                .orElseThrow(() -> new NurseNotFoundException("Profile not found"));
        if (!profile.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedAccessException("You can only update your own profile");
        }
        return profile;
    }

    @Recover
    public NurseProfileDTO recover(RuntimeException e, NurseProfileDTO dto) {
        if (e instanceof OptimisticLockException ||
                e instanceof ObjectOptimisticLockingFailureException) {
            throw new ConcurrentModificationException(
                    "Profile update failed due to concurrent modifications. " +
                            "Please refresh the profile and try again. " +
                            "Profile ID: " + dto.getNurseId());
        }

        if (e instanceof TransactionSystemException) {
            throw new ServiceException(
                    "Database error during profile update. Please try again later.",
                    e);
        }

        throw new ServiceException(
                "Profile update failed after multiple attempts. " +
                        "Please contact support if the issue persists. " +
                        "Error: " + e.getMessage(),
                e);
    }
}
//   // update
//    @Transactional
//    public NurseProfileDTO updateProfile(NurseProfileDTO dto){
//
//        User currentUser = securityUtils.getCurrentUser()
//                .orElseThrow(()->new UnauthorizedAccessException("User not Authenticated"));
//
//        NurseProfile existingProfile=nurseProfileRepository.findById(dto.getNurseId())
//                .orElseThrow(()->new UnauthorizedAccessException(" Profile Not Found"));
//
//        if(!existingProfile.getUser().getId().equals(currentUser.getId())){
//            throw new UnauthorizedAccessException("You can only update uour own  profile");
//        }
//
//
//         nurseProfileMapper.updateEntity(dto , existingProfile);
//        return nurseProfileMapper.toDto(nurseProfileRepository.save(existingProfile));
//    }



//@Transactional
//public NurseProfileDTO createProfile(NurseProfileDTO dto) {
//    User currentUser =securityUtils.getCurrentUser()
//            .orElseThrow(() -> new UnauthorizedAccessException("User not Authenticated"));
//
//    if (currentUser.getRole() != Role.NURSE) {
//        throw new UnauthorizedAccessException("Only Nurse can create Profile");
//    }
//
//    // Validate license number is provided
//    if (dto.getLicenseNumber() == null || dto.getLicenseNumber().isBlank()) {
//        throw new IllegalArgumentException("License number is required");
//    }
//
//    NurseProfile nurseProfile = nurseProfileMapper.toEntity(dto);
//    nurseProfile.setUser(currentUser);
//    nurseProfile.setNurseId(currentUser.getId()); // Set the ID explicitly
//    nurseProfile.setStatus(ProfileStatus.PENDING);
//
//    return nurseProfileMapper.toDto(nurseProfileRepository.save(nurseProfile));
//}