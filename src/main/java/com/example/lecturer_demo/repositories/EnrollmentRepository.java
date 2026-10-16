package com.example.lecturer_demo.repositories;

import com.example.lecturer_demo.models.Student;
import com.example.lecturer_demo.models.StudentEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<StudentEnrollment, Long> {
    boolean existsByCourseIdAndStudentId(Long courseId, Long studentId);
    Optional<StudentEnrollment> findByCourseIdAndStudentId(Long courseId, Long studentId);

    @Query("SELECT e.student FROM StudentEnrollment e WHERE e.course.id = :courseId AND LOWER(e.student.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Student> searchStudentsByCourseIdAndName(@Param("courseId") Long courseId, @Param("search") String search);
}