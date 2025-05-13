package com.example.Studentmanagement_system;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

// UNIT TEST

// mockito integration
@ExtendWith(MockitoExtension.class)
public class StudentServiceUnitTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setName("Benny Andersson");
        student.setEmail("Benny.A@email.com");
    }

    @Test
    void getAllStudentsShouldReturnAllStudents() {
        // Arrange
        when(studentRepository.findAll()).thenReturn(Arrays.asList(student));

        // Act
        List<Student> students = studentService.getAllStudents();

        // Assert
        assertEquals(1, students.size());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void getStudentByIdShouldReturnStudentWhenIdExists() {
        // Arrange
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        // Act
        Student foundStudent = studentService.getStudentById(1L);

        // Assert
        assertNotNull(foundStudent);
        assertEquals(student.getName(), foundStudent.getName());
    }

    @Test
    void getStudentByIdShouldThrowExceptionWhenIdDoesNotExist() {
        // ARRANGE
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () -> studentService.getStudentById(1L));
    }

    @Test
    void createStudentShouldReturnSavedStudent() {
        // ARRANGE
        when(studentRepository.existsByEmail(student.getEmail())).thenReturn(false);
        when(studentRepository.save(student)).thenReturn(student);

        // ACT
        Student savedStudent = studentService.createStudent(student);

        // ASSERT
        assertNotNull(savedStudent);
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void createStudentShouldThrowExceptionWhenEmailExists() {
        // ARRANGE
        when(studentRepository.existsByEmail(student.getEmail())).thenReturn(true);

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () -> studentService.createStudent(student));
    }
}