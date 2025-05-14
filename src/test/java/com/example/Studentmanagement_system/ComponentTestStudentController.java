package com.example.Studentmanagement_system;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// COMPONENT TEST

// using mock to isolate the controller class from service and repository. i chose to test these methods bc they're
// all well used scenarios.

// MockMvc to test API calls isolated from other classes.
@WebMvcTest(StudentController.class)
public class ComponentTestStudentController {

    @Autowired
    private MockMvc mockMvc;

    // creating mocked dependency of spring-components in StudentService
    @MockBean
    private StudentService studentService;

    // ObjectMapper is used to convert java-objects to json-strings with HTTP post calls
    // and to validate data from API answers
    @Autowired
    private ObjectMapper objectMapper;

    // creating a test student and verifying that you get the correct answer in return
    @Test
    void createStudentShouldReturnSavedStudent() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Karl Karlsson");
        student.setEmail("Karl.K@email.com");

        // given is used to define the acting from the mocked services, as StudentService.createStudent
        given(studentService.createStudent(any(Student.class))).willAnswer(invocation -> {
            Student s = invocation.getArgument(0);
            s.setId(1L);
            return s;
        });

        // using jsonPath to verify that the response contain the expected value, ex student name
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void getAllStudentsShouldReturnAllStudents() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Karl Karlsson");
        student.setEmail("Karl.K@email.com");

        given(studentService.getAllStudents()).willReturn(Arrays.asList(student));

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(student.getName()));

    }

    @Test
    void getStudentByIdShouldReturnStudentWhenIdExists() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Karl Karlsson");
        student.setEmail("Karl.K@email.com");

        given(studentService.getStudentById(1L)).willReturn(student);

        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(student.getName()));
    }

}