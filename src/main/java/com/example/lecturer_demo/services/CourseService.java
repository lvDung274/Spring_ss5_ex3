package com.example.lecturer_demo.services;

import com.example.lecturer_demo.dto.*;
import com.example.lecturer_demo.models.Course;
import com.example.lecturer_demo.models.CourseStatus;
import com.example.lecturer_demo.models.Instructor;
import com.example.lecturer_demo.repositories.CourseRepository;
import com.example.lecturer_demo.repositories.InstructorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;

    public CourseService(CourseRepository courseRepository, InstructorRepository instructorRepository) {
        this.courseRepository = courseRepository;
        this.instructorRepository = instructorRepository;
    }

    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(course -> new CourseResponse(
                        course.getId(),
                        course.getTitle(),
                        course.getStatus(),
                        new CourseInstructorResponse(
                                course.getInstructor().getId(),
                                course.getInstructor().getName()
                        )
                ))
                .toList();
    }

    public CourseResponse getCourseResponseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học với ID: " + id));

        return new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getStatus(),
                new CourseInstructorResponse(
                        course.getInstructor().getId(),
                        course.getInstructor().getName()
                )
        );
    }

    public void createCourse(CourseCreateRequest req) {
        Instructor instructor = instructorRepository.findById(req.instructorId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giảng viên với ID: " + req.instructorId()));

        Course course = new Course();
        course.setTitle(req.title());
        course.setStatus(CourseStatus.valueOf(req.status()));
        course.setInstructor(instructor);

        courseRepository.save(course);
    }

    public void updateCourse(Long id, CourseUpdateRequest req) {
        Course existing = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học với ID: " + id));

        Instructor instructor = instructorRepository.findById(req.instructorId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giảng viên với ID: " + req.instructorId()));

        existing.setTitle(req.title());
        existing.setStatus(CourseStatus.valueOf(req.status()));
        existing.setInstructor(instructor);

        courseRepository.save(existing);
    }

    public void deleteCourseById(Long id) {
        Course existing = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học với ID: " + id));
        courseRepository.delete(existing);
    }
    public PageResponse<CourseResponse> getPagedCoursesByStatus(
            int page, int size, String sortBy, Sort.Direction direction, CourseStatus status) {
        if (page < 0) {
            page = 0;
        }

        if (sortBy == null || sortBy.trim().isEmpty()) {
            sortBy = "id";
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<Course> coursePage = courseRepository.findAllByStatus(status, pageable);

        Page<CourseResponse> courseResponsePage = coursePage.map(course -> {
            CourseInstructorResponse instructorResponse = null;
            if (course.getInstructor() != null) {
                instructorResponse = new CourseInstructorResponse(
                        course.getInstructor().getId(),
                        course.getInstructor().getName()
                );
            }

            return new CourseResponse(
                    course.getId(),
                    course.getTitle(),
                    course.getStatus(),
                    instructorResponse
            );
        });

        return PageResponse.<CourseResponse>builder()
                .items(courseResponsePage.getContent())
                .page(courseResponsePage.getNumber())
                .size(courseResponsePage.getSize())
                .totalItems(courseResponsePage.getTotalElements())
                .totalPages(courseResponsePage.getTotalPages())
                .isLast(courseResponsePage.isLast())
                .build();
    }
}
