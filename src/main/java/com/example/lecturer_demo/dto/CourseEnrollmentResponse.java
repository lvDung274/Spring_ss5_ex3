package com.example.lecturer_demo.dto;

import java.time.LocalDateTime;

public record CourseEnrollmentResponse(
        Long studentId,
        Long courseId,
        LocalDateTime enrolledAt
) {}