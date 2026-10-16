package com.example.lecturer_demo.controllers;

import com.example.lecturer_demo.dto.ApiResponse;
import com.example.lecturer_demo.dto.CourseCreateRequest;
import com.example.lecturer_demo.dto.CourseResponse;
import com.example.lecturer_demo.dto.CourseUpdateRequest;
import com.example.lecturer_demo.services.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/app/course")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // Đã chuyển đổi kiểu trả về từ List<Course> sang List<CourseResponse>
    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseResponse>>> getAllCourses() {
        try {
            List<CourseResponse> courses = courseService.getAllCourses();
            return ResponseEntity.ok(new ApiResponse<>(true, "Đã truy xuất thành công", courses));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponse>> getCourseById(@PathVariable Long id) {
        try {
            CourseResponse course = courseService.getCourseResponseById(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Đã truy xuất thành công", course));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createCourse(@RequestBody CourseCreateRequest request) {
        try {
            courseService.createCourse(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Tạo thành công", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateCourse(@PathVariable Long id, @RequestBody CourseUpdateRequest request) {
        try {
            courseService.updateCourse(id, request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Cập nhật thành công", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCourseById(@PathVariable Long id) {
        try {
            courseService.deleteCourseById(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Xóa thành công khóa học", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(ApiResponse.error(e.getMessage()));
        }
    }
}