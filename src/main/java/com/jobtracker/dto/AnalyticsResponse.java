package com.jobtracker.dto;

import java.util.List;
import java.util.Map;

public record AnalyticsResponse(
    long totalApplications,
    Map<String, Long> statusBreakdown,   // e.g. {"APPLIED": 10, "OFFER": 2}
    List<WeeklyCount> weeklyApplications, // last 8 weeks
    double successRate                    // offers / total * 100
) {
    public record WeeklyCount(int week, long count) {}
}