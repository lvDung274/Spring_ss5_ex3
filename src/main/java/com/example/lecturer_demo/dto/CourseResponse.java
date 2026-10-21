package com.example.lecturer_demo.dto;

import com.example.lecturer_demo.models.CourseStatus;
import lombok.*;


public record CourseResponse(
        Long id,
        String title,
        CourseStatus status,
        CourseInstructorResponse instructor
) {}