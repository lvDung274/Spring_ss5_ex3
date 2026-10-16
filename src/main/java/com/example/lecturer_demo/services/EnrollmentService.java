package com.example.lecturer_demo.services;

import com.example.lecturer_demo.dto.CourseEnrollmentResponse;
import com.example.lecturer_demo.dto.EnrollCourseRequest;
import com.example.lecturer_demo.dto.EnrollmentDetail;
import com.example.lecturer_demo.models.Course;
import com.example.lecturer_demo.models.CourseStatus;
import com.example.lecturer_demo.models.Student;
import com.example.lecturer_demo.models.StudentEnrollment;
import com.example.lecturer_demo.repositories.CourseRepository;
import com.example.lecturer_demo.repositories.EnrollmentRepository;
import com.example.lecturer_demo.repositories.InstructorRepository;
import com.example.lecturer_demo.repositories.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final StudentRepository studentRepository;

    // Constructor Injection
    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             CourseRepository courseRepository,
                             InstructorRepository instructorRepository,
                             StudentRepository studentRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.instructorRepository = instructorRepository;
        this.studentRepository = studentRepository;
    }

    public List<StudentEnrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    public StudentEnrollment getEnrollmentById(long id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin đăng ký"));
    }

    public void deleteEnrollmentById(long id) {
        enrollmentRepository.deleteById(id);
    }

    public CourseEnrollmentResponse enrollCourse(Long courseId, Long studentId) {
        boolean isCourseActive = courseRepository.existsByIdAndStatus(courseId, CourseStatus.ACTIVE);
        if (!isCourseActive) {
            throw new RuntimeException("Khóa học không tồn tại hoặc không ở trạng thái ACTIVE!");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên với ID: " + studentId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học với ID: " + courseId));

        boolean isAlreadyEnrolled = enrollmentRepository.existsByCourseIdAndStudentId(courseId, studentId);
        if (isAlreadyEnrolled) {
            throw new RuntimeException("Sinh viên này đã đăng ký khóa học này rồi!");
        }

        StudentEnrollment newEnrollment = new StudentEnrollment();
        newEnrollment.setStudent(student);
        newEnrollment.setCourse(course);

        StudentEnrollment savedEnrollment = enrollmentRepository.save(newEnrollment);

        return new CourseEnrollmentResponse(
                savedEnrollment.getStudent().getId(),
                savedEnrollment.getCourse().getId(),
                savedEnrollment.getEnrolledAt()
        );
    }

    public void removeStudentFromCourse(Long courseId, Long studentId) {
        StudentEnrollment enrollment = enrollmentRepository.findByCourseIdAndStudentId(courseId, studentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin đăng ký của sinh viên trong khóa học này!"));

        enrollmentRepository.delete(enrollment);
    }

    public List<Student> searchStudentsInCourse(Long courseId, String search) {
        if (!courseRepository.existsById(courseId)) {
            throw new RuntimeException("Không tìm thấy khóa học với ID: " + courseId);
        }
        return enrollmentRepository.searchStudentsByCourseIdAndName(courseId, search);
    }
}