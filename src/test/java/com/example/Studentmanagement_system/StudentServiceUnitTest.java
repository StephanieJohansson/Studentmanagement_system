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

// these are some methods i think is important. to create a student and also test that you cant create a new student
// with an already existing email. Get all students, get student by ID along with the scenario that the student ID
// doesnt exists.

// mockito integration
@ExtendWith(MockitoExtension.class)
public class StudentServiceUnitTest {

    // mocked repo, this is the repo that will be tested
    @Mock
    private StudentRepository studentRepository;

    // creating instance of the test-repo that automatically inject mocked versions of the dependencies
    // this is great to isolate the test bc i dont need to manually handle dependency injection
    @InjectMocks
    private StudentService studentService;

    private Student student;

    // set up for the test student
    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setName("Benny Andersson");
        student.setEmail("Benny.A@email.com");
    }

    // when findAll is called return the list with students which is in this case just 1
    // assert is verifying that 1 is correct and it run 1 time
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