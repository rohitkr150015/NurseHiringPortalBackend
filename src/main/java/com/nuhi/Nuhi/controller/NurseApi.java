package com.nuhi.Nuhi.controller;


import com.nuhi.Nuhi.dto.ApprovalDTO;
import com.nuhi.Nuhi.dto.NurseProfileDTO;

import com.nuhi.Nuhi.service.NurseService;

import com.nuhi.Nuhi.util.ValidationGroups;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nurses")
@RequiredArgsConstructor
public class NurseApi {


    private final NurseService nurseService;

    @PostMapping("/profile")
    @PreAuthorize("hasRole('NURSE')")
    public ResponseEntity<NurseProfileDTO> createProfile(@RequestBody  @Validated(ValidationGroups.CreateValidationGroup.class) NurseProfileDTO nurseProfileDTO) {
        return ResponseEntity.ok(nurseService.createProfile(nurseProfileDTO));
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('NURSE')")
    public ResponseEntity<NurseProfileDTO> updateProfile(@RequestBody @Validated(ValidationGroups.UpdateValidationGroup.class) NurseProfileDTO nurseProfileDTO) {
        return ResponseEntity.ok(nurseService.updateProfile(nurseProfileDTO));

    }

    @GetMapping
    public ResponseEntity<List<NurseProfileDTO>> getApprovedNurseProfile(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String specialization){


        return ResponseEntity.ok(nurseService.findApprovedNurses(location , specialization));
    }

    @PutMapping("/admin/nurses/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> approveProfile(
            @PathVariable Long id,
            @RequestBody ApprovalDTO approvalDTO){

        nurseService.approveProfile(id , approvalDTO.profileStatus());
        return ResponseEntity.ok().build();


    }






}
