package com.polevchshikov.studentapi.repository;

import com.polevchshikov.studentapi.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
