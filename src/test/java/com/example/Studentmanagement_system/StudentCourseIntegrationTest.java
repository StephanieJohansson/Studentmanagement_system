package com.example.Studentmanagement_system;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

// INTEGRATION TEST

// add course to student and update student and course is the most important for my integrationtest since you're most
// likely to add a course and update a student in a real life scenario etc.

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// using my h2 test database
@ActiveProfiles("test")
public class StudentCourseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void addCourseToStudent() {
        // ARRANGE
        // create and save student
        Student student = Student.builder()
                .name("Banarne Olsson")
                .email("Banarne.O@emal.com")
                .build();
        student = studentRepository.save(student);

        // create and save course
        Course course = Course.builder()
                .name("Math 101")
                .code("MATH101")
                .build();
        course = courseRepository.save(course);

        // ACT
        // add course to student via REST API med PUT
        ResponseEntity<Student> response = restTemplate.exchange(
                "/api/students/" + student.getId() + "/courses",
                HttpMethod.PUT,
                new HttpEntity<>(List.of(course.getId())),
                Student.class
        );

        // ASSERT
        // control status 200 OK
        assertThat(response.getStatusCode().value()).isEqualTo(200);

        // control that course is linked to student
        Student updatedStudent = response.getBody();
        assertThat(updatedStudent).isNotNull();
        assertThat(updatedStudent.getCourses())
                .hasSize(1)
                .extracting(Course::getName)
                .containsExactly("Math 101");
    }

    @Test
    void updateStudentAndCourse() {
        // ARRANGE
        // creating student and course
        Student student = studentRepository.save(
                Student.builder()
                        .name("Stephen King")
                        .email("King@email.com")
                        .build()
        );

        Course course = courseRepository.save(
                Course.builder()
                        .name("History 101")
                        .code("HIST101")
                        .build()
        );

        // link course to student via PUT /api/students/{id}/courses
        ResponseEntity<Student> linkResponse = restTemplate.exchange(
                "/api/students/" + student.getId() + "/courses",
                HttpMethod.PUT,
                new HttpEntity<>(List.of(course.getId())),
                Student.class
        );
        assertThat(linkResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // ACT
        // update student data
        Map<String, Object> studentUpdates = new HashMap<>();
        studentUpdates.put("name", "Stephen King updated");
        studentUpdates.put("email", "King.updated@email.com");
        // keeping the same course for this
        studentUpdates.put("courseIds", List.of(course.getId()));

        ResponseEntity<Student> studentUpdateResponse = restTemplate.exchange(
                "/api/students/" + student.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(studentUpdates),
                Student.class
        );

        // update course data
        Course updatedCourseData = Course.builder()
                .name("Philosophy 104")
                .code("PHIL104")
                .build();

        ResponseEntity<Course> courseUpdateResponse = restTemplate.exchange(
                "/courses/" + course.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(updatedCourseData),
                Course.class
        );

        // ASSERT
        // verify result both student update and course update
        assertThat(studentUpdateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Student updatedStudent = studentUpdateResponse.getBody();
        assertThat(updatedStudent).isNotNull();
        assertThat(updatedStudent.getName()).isEqualTo("Stephen King updated");
        assertThat(updatedStudent.getEmail()).isEqualTo("King.updated@email.com");

        assertThat(courseUpdateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Course updatedCourse = courseUpdateResponse.getBody();
        assertThat(updatedCourse).isNotNull();
        assertThat(updatedCourse.getName()).isEqualTo("Philosophy 104");
        assertThat(updatedCourse.getCode()).isEqualTo("PHIL104");

        // verifying that relationship still exists with updated info
        ResponseEntity<Student> finalStudentResponse = restTemplate.getForEntity(
                "/api/students/" + student.getId(),
                Student.class
        );

        Student finalStudent = finalStudentResponse.getBody();
        assertThat(finalStudent).isNotNull();
        assertThat(finalStudent.getCourses())
                .hasSize(1)
                .extracting(Course::getName)
                .containsExactly("Philosophy 104");
    }

}