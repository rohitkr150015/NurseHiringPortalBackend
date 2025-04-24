package com.nuhi.Nuhi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminActionDTO {

        private Long actionId;
        private Long adminId;

        @Size(max = 50)
        private String actionType;

        private Long targetId;

        @Size(max = 1000)
        private String details;

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime timestamp;

        // For UI
        private String adminName;
    }



