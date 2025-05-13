package com.example.Studentmanagement_system;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final CourseRepository CourseRepository;

    public List<Student> getAllStudents(){
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id){
        return studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));
    }

    public Student createStudent(Student student){
        if(studentRepository.existsByEmail(student.getEmail())){
            throw new IllegalArgumentException("Student already exists with email: " + student.getEmail());
        }
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student studentDetails){
        Student student = getStudentById(id);
        student.setName(studentDetails.getName());
        student.setEmail(studentDetails.getEmail());
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id){
        Student student = getStudentById(id);
        studentRepository.delete(student);
    }

    public Student addCoursesToStudent(Long studentId, List<Long> courseIds) {
        // get student from database
        Student student = getStudentById(studentId);
        // get course by id
        List<Course> courses = CourseRepository.findAllById(courseIds);
        // add courses to students courses
        student.getCourses().addAll(courses);
        // save updated student
        return studentRepository.save(student);
    }

}
