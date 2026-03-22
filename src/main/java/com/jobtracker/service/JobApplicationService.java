package com.jobtracker.service;

import com.jobtracker.dto.AnalyticsResponse;
import com.jobtracker.dto.AnalyticsResponse.WeeklyCount;
import com.jobtracker.dto.JobDTOs.*;
import com.jobtracker.model.ApplicationStatus;
import com.jobtracker.model.JobApplication;
import com.jobtracker.model.User;
import com.jobtracker.repository.JobApplicationRepository;
import com.jobtracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final JobApplicationRepository jobRepo;
    private final UserRepository userRepository;

    // ── Helper: get logged-in user ─────────────────────────────
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ── CREATE ─────────────────────────────────────────────────
    public JobResponse create(JobRequest request) {
        User user = getCurrentUser();

        JobApplication job = JobApplication.builder()
                .user(user)
                .company(request.company())
                .role(request.role())
                .status(request.status())
                .jobLink(request.jobLink())
                .notes(request.notes())
                .appliedDate(request.appliedDate())
                .build();

        return JobResponse.from(jobRepo.save(job));
    }

    // ── READ ALL ───────────────────────────────────────────────
    public List<JobResponse> getAll(ApplicationStatus status) {
        Long userId = getCurrentUser().getId();

        List<JobApplication> jobs = (status != null)
                ? jobRepo.findByUserIdAndStatusOrderByCreatedAtDesc(userId, status)
                : jobRepo.findByUserIdOrderByCreatedAtDesc(userId);

        return jobs.stream()
                .map(JobResponse::from)
                .collect(Collectors.toList());
    }

    // ── READ ONE ───────────────────────────────────────────────
    public JobResponse getById(Long id) {
        Long userId = getCurrentUser().getId();
        JobApplication job = jobRepo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Job application not found"));
        return JobResponse.from(job);
    }

    // ── UPDATE ─────────────────────────────────────────────────
    public JobResponse update(Long id, JobRequest request) {
        Long userId = getCurrentUser().getId();
        JobApplication job = jobRepo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Job application not found"));

        job.setCompany(request.company());
        job.setRole(request.role());
        job.setStatus(request.status());
        job.setJobLink(request.jobLink());
        job.setNotes(request.notes());
        if (request.appliedDate() != null) {
            job.setAppliedDate(request.appliedDate());
        }

        return JobResponse.from(jobRepo.save(job));
    }

    // ── DELETE ─────────────────────────────────────────────────
    public void delete(Long id) {
        Long userId = getCurrentUser().getId();
        JobApplication job = jobRepo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Job application not found"));
        jobRepo.delete(job);
    }

    // ── ANALYTICS ─────────────────────────────────────────────
    public AnalyticsResponse getAnalytics() {
        Long userId = getCurrentUser().getId();

        // Total count
        long total = jobRepo.countByUserId(userId);

        // Status breakdown
        Map<String, Long> breakdown = new LinkedHashMap<>();
        for (ApplicationStatus s : ApplicationStatus.values()) {
            breakdown.put(s.name(), 0L);
        }
        jobRepo.countByStatusForUser(userId).forEach(row -> {
            breakdown.put(((ApplicationStatus) row[0]).name(), (Long) row[1]);
        });

        // Weekly counts (last 8 weeks)
        LocalDate eightWeeksAgo = LocalDate.now().minusWeeks(8);
        List<WeeklyCount> weekly = jobRepo.weeklyCountsSince(userId, eightWeeksAgo)
                .stream()
                .map(row -> new WeeklyCount(((Number) row[0]).intValue(), (Long) row[1]))
                .collect(Collectors.toList());

        // Success rate
        long offers = breakdown.getOrDefault("OFFER", 0L);
        double successRate = total > 0 ? (double) offers / total * 100 : 0;

        return new AnalyticsResponse(total, breakdown, weekly, successRate);
    }
}