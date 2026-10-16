package com.example.lecturer_demo.controllers;

import com.example.lecturer_demo.dto.ApiResponse;
import com.example.lecturer_demo.dto.CourseEnrollmentRequest;
import com.example.lecturer_demo.dto.CourseEnrollmentResponse;
import com.example.lecturer_demo.models.Student;
import com.example.lecturer_demo.services.EnrollmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/app/courses")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/{courseId}/enrollments")
    public ResponseEntity<ApiResponse<CourseEnrollmentResponse>> enrollStudent(
            @PathVariable Long courseId,
            @RequestBody CourseEnrollmentRequest request) {
        try {
            CourseEnrollmentResponse response = enrollmentService.enrollCourse(courseId, request.studentId());            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Đăng ký khóa học thành công", response));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{courseId}/students/{studentId}")
    public ResponseEntity<ApiResponse<Void>> removeStudentFromCourse(
            @PathVariable Long courseId,
            @PathVariable Long studentId) {
        try {
            enrollmentService.removeStudentFromCourse(courseId, studentId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiResponse<>(true, "Xóa sinh viên khỏi khóa học thành công", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{courseId}/enrollments/students")
    public ResponseEntity<ApiResponse<List<Student>>> searchStudentsInCourse(
            @PathVariable Long courseId,
            @RequestParam(defaultValue = "") String search) {
        try {
            List<Student> students = enrollmentService.searchStudentsInCourse(courseId, search);
            return ResponseEntity.ok(new ApiResponse<>(true, "Tìm kiếm sinh viên thành công", students));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(ApiResponse.error(e.getMessage()));
        }
    }
}