INSERT INTO instructor(first_name, last_name, years_of_experience) VALUES ('Johny', 'Joystick', 5);
INSERT INTO instructor(first_name, last_name, years_of_experience) VALUES ('Nurs', 'Et', 7);

INSERT INTO course(name, start_date, end_date, instructor_id) VALUES ('Johny Joystick''s programming course', '01.07.2021', '07.07.2021', 1);
INSERT INTO course(name, start_date, end_date, instructor_id) VALUES ('Nurs Et''s cooking course', '01.07.2021', '01.07.2022', 2);

INSERT INTO lesson(name, day_of_week, time, course_id) VALUES ('Molecular cooking', 'Monday', '17:00', 1);
INSERT INTO lesson(name, day_of_week, time, course_id) VALUES ('AaDS', 'Thursday', '20:00', 1);
INSERT INTO lesson(name, day_of_week, time, course_id) VALUES ('Databases', 'Friday', '9:00', 1);

INSERT INTO student(first_name, last_name, groupp) VALUES ('Aidar', 'Ibragimov', '11-004');
INSERT INTO student(first_name, last_name, groupp) VALUES ('Emma', 'Mackey', '11-004');

INSERT INTO students_courses(student_id, course_id) VALUES (1,1);
INSERT INTO students_courses(student_id, course_id) VALUES (2,1);
INSERT INTO students_courses(student_id, course_id) VALUES (2,2);