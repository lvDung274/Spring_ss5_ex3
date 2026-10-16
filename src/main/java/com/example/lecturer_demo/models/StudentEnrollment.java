package com.example.lecturer_demo.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "student_enrollments", schema = "public")
@Builder
public class StudentEnrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "enrolled_at")
    private LocalDateTime enrolledAt;

    @PrePersist
    public void oncreate() {
        this.enrolledAt = LocalDateTime.now();
    }
}
