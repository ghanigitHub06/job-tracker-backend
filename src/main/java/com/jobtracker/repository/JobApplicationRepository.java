package com.jobtracker.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jobtracker.model.ApplicationStatus;
import com.jobtracker.model.JobApplication;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long>{
	// Fetch all jobs for a user, newest first
	List<JobApplication> findByUserIdOrderByCreatedAtDesc(Long userId);
	  // Filter by status
    List<JobApplication> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, ApplicationStatus status);
 // Single job (scoped to user for security)
    Optional<JobApplication> findByIdAndUserId(Long id, Long userId);
 
    // Count by status for pie chart
    @Query("SELECT j.status, COUNT(j) FROM JobApplication j WHERE j.user.id = :userId GROUP BY j.status")
    List<Object[]> countByStatusForUser(@Param("userId") Long userId);
 
    // Weekly application counts (last 8 weeks)
    @Query("SELECT WEEK(j.appliedDate), COUNT(j) FROM JobApplication j " +
           "WHERE j.user.id = :userId AND j.appliedDate >= :since GROUP BY WEEK(j.appliedDate)")
    List<Object[]> weeklyCountsSince(@Param("userId") Long userId, @Param("since") LocalDate since);
 
    // Total count
    long countByUserId(Long userId);
}
 
