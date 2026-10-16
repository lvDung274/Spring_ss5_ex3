package com.example.lecturer_demo.repositories;

import com.example.lecturer_demo.models.Course;
import com.example.lecturer_demo.models.CourseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByIdAndStatus(Long id, CourseStatus status);
}