package com.student.studentmanagementsystem.repository;

import com.student.studentmanagementsystem.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByEmailAddressIgnoreCase(String emailAddress);
    List<Student> findByCourseNameIgnoreCase(String courseName);
}
