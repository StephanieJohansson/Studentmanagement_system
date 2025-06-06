/*creating a schema with the tables manually to use in my h2 test database#*/

CREATE TABLE students (
                          id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                          email VARCHAR(255) NOT NULL UNIQUE,
                          name VARCHAR(255) NOT NULL
);

CREATE TABLE courses (
                         id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                         code VARCHAR(255) NOT NULL UNIQUE,
                         name VARCHAR(255) NOT NULL
);

CREATE TABLE student_course (
                                student_id BIGINT NOT NULL,
                                course_id BIGINT NOT NULL,
                                PRIMARY KEY (student_id, course_id),
                                FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
                                FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);