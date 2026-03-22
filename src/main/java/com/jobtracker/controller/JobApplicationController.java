package com.jobtracker.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobtracker.dto.AnalyticsResponse;
import com.jobtracker.dto.JobDTOs.JobRequest;
import com.jobtracker.dto.JobDTOs.JobResponse;
import com.jobtracker.model.ApplicationStatus;
import com.jobtracker.service.JobApplicationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationService jobService;

    // POST /api/jobs — create new application
    @PostMapping
    public ResponseEntity<JobResponse> create(@Valid @RequestBody JobRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jobService.create(request));
    }

    // GET /api/jobs — get all (optional ?status=APPLIED filter)
    @GetMapping
    public ResponseEntity<List<JobResponse>> getAll(
            @RequestParam(required = false) ApplicationStatus status) {
        return ResponseEntity.ok(jobService.getAll(status));
    }

    // GET /api/jobs/{id} — get single
    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getById(id));
    }

    // PUT /api/jobs/{id} — update
    @PutMapping("/{id}")
    public ResponseEntity<JobResponse> update(@PathVariable Long id,
                                               @Valid @RequestBody JobRequest request) {
        return ResponseEntity.ok(jobService.update(id, request));
    }

    // DELETE /api/jobs/{id} — delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        jobService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/jobs/analytics — dashboard data
    @GetMapping("/analytics")
    public ResponseEntity<AnalyticsResponse> getAnalytics() {
        return ResponseEntity.ok(jobService.getAnalytics());
    }
}