package com.service;

import com.dto.ApiResponse;

public interface ParentService {
    ApiResponse getChildGrades(String parentId);
    ApiResponse getChildAttendance(String parentId);
    ApiResponse getChildFeeStatus(String parentId);
    ApiResponse getChildResults(String parentId);
    ApiResponse getParentDashboard(String parentId);
    ApiResponse getParentProfile(String parentId);
    ApiResponse getChildTimetable(String parentId);
    ApiResponse getChildUpcomingAssignments(String parentId);
}
