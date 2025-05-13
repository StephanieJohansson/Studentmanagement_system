package com.example.Studentmanagement_system;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;

    public List<Course> getAllCourses(){
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id){
        return courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + id));
    }

    public Course createCourse(Course course){
        if(courseRepository.existsByCode(course.getCode())){
            throw new IllegalArgumentException("Course already exists with code: " + course.getCode());
        }
        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, Course courseDetails){
        Course course = getCourseById(id);
        course.setName(courseDetails.getName());
        course.setCode(courseDetails.getCode());
        return courseRepository.save(course);
    }

    public void deleteCourse(Long id){
        Course course = getCourseById(id);
        courseRepository.delete(course);
    }
}
