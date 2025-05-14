package com.example.Studentmanagement_system;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// boolean to control if there's already a student with this email or not
@Repository
public interface StudentRepository extends JpaRepository<Student, Long>{
    boolean existsByEmail(String email);
}

