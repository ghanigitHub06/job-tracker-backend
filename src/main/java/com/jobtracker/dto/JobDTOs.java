package com.jobtracker.dto;

import com.jobtracker.model.ApplicationStatus;
import com.jobtracker.model.JobApplication;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class JobDTOs {

    //  Request DTO (what client sends)
    public record JobRequest(
        @NotBlank(message = "Company is required")
        String company,

        @NotBlank(message = "Role is required")
        String role,

        @NotNull(message = "Status is required")
        ApplicationStatus status,

        String jobLink,
        String notes,
        LocalDate appliedDate
    ) {}

    //Response DTO (what server sends back) 
    public record JobResponse(
        Long id,
        String company,
        String role,
        ApplicationStatus status,
        String jobLink,
        String notes,
        LocalDate appliedDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        // Static factory method to convert entity → DTO
        public static JobResponse from(JobApplication job) {
            return new JobResponse(
                job.getId(),
                job.getCompany(),
                job.getRole(),
                job.getStatus(),
                job.getJobLink(),
                job.getNotes(),
                job.getAppliedDate(),
                job.getCreatedAt(),
                job.getUpdatedAt()
            );
        }
    }
}