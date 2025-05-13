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

@WebMvcTest(StudentController.class)
public class ComponentTestStudentController {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createStudentShouldReturnSavedStudent() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Karl Karlsson");
        student.setEmail("Karl.K@email.com");

        given(studentService.createStudent(any(Student.class))).willAnswer(invocation -> {
            Student s = invocation.getArgument(0);
            s.setId(1L);
            return s;
        });

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