CREATE TABLE instructor (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    years_of_experience INTEGER
);

CREATE TABLE course (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    start_date CHAR(10),
    end_date CHAR(10),
    instructor_id INTEGER REFERENCES instructor(id) NOT NULL
);

CREATE TABLE lesson (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    day_of_week VARCHAR(9),
    time VARCHAR(20),
    course_id INTEGER REFERENCES course(id) NOT NULL
);

CREATE TABLE student (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    groupp VARCHAR(255)
);

CREATE TABLE students_courses (
    student_id INTEGER REFERENCES student(id),
    course_id INTEGER REFERENCES course(id),
    PRIMARY KEY(student_id, course_id)
);