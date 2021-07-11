package ru.itis.repository.impl;

import ru.itis.models.Course;
import ru.itis.models.Instructor;
import ru.itis.models.Student;
import ru.itis.repository.CoursesRepository;
import ru.itis.utils.LocalDateTimeUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

public class CoursesRepositoryJDBCImpl implements CoursesRepository {

	private final DataSource dataSource;

	//language=SQL
	private static final String SQL_SELECT_ALL = "SELECT i.id AS instructor_id, s.first_name as s_first_name,s.last_name as s_last_name, s.id as student_id, * FROM course c " +
			"LEFT JOIN instructor i ON i.id = c.instructor_id " +
			"JOIN students_courses sc ON c.id = sc.course_id " +
			"JOIN student s ON sc.student_id = s.id ORDER BY c.id";
	//language=SQL
	private static final String SQL_SELECT_BY_ID = "SELECT i.id AS instructor_id, s.first_name as s_first_name,s.last_name as s_last_name, s.id as student_id, * FROM course c "+
			"LEFT JOIN instructor i ON i.id = c.instructor_id " +
			"JOIN students_courses sc ON c.id = sc.course_id " +
			"JOIN student s ON sc.student_id = s.id WHERE c.id = ? ORDER BY c.id";
	//language=SQL
	private static final String SQL_SELECT_BY_NAME = "SELECT i.id AS instructor_id, s.first_name as s_first_name,s.last_name as s_last_name, s.id as student_id, * FROM course c "+
			"LEFT JOIN instructor i ON i.id = c.instructor_id " +
			"JOIN students_courses sc ON c.id = sc.course_id " +
			"JOIN student s ON sc.student_id = s.id WHERE c.name = ? ORDER BY c.id";
	//language=SQL
	private static final String SQL_UPDATE_BY_ID = "UPDATE course set name = ?, start_date = ?, end_date = ?, instructor_id = ? where id = ?";
	//language=SQL
	private static final String SQL_INSERT = "INSERT INTO course(name, start_date, end_date, instructor_id) VALUES (?,?,?,?)";


	public CoursesRepositoryJDBCImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	private final Function<ResultSet, Course> courseRowMapper = row -> {
		try {
			int id = row.getInt("id");
			String name = row.getString("name");
			LocalDate startDate = LocalDateTimeUtils.localDateOfDateString(row.getString("start_date"));
			LocalDate endDate = LocalDateTimeUtils.localDateOfDateString(row.getString("end_date"));
			Course course = new Course(id, name, startDate, endDate);
			course.setStudents(new ArrayList<>());
			return course;
		} catch (SQLException e) {
			throw new IllegalArgumentException(e);
		}
	};

	private final Function<ResultSet, Student> studentRowMapper = row -> {
		try {
			int id = row.getInt("student_id");
			String firstName = row.getString("s_first_name");
			String lastName = row.getString("s_last_name");
			String group = row.getString("groupp");
			Student student = new Student(id, firstName, lastName, group);
			student.setCourses(new ArrayList<>());
			return student;
		}
		catch (SQLException e){
			throw new IllegalArgumentException(e);
		}
	};

	private final Function<ResultSet, Instructor> instructorRowMapper = row -> {
		try {
			int id = row.getInt("instructor_id");
			String firstName = row.getString("first_name");
			String lastName = row.getString("last_name");
			int yearsOfExperience = row.getInt("years_of_experience");
			Instructor instructor = new Instructor(id, firstName, lastName, yearsOfExperience);
			instructor.setCourses(new ArrayList<>());
			return instructor;
		}
		catch (SQLException e){
			throw new IllegalArgumentException(e);
		}
	};



	@Override
	public List<Course> findAll() {
		List<Course> courses = new ArrayList<>();
		try (Connection connection = dataSource.getConnection();
			 PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL);
			 ResultSet resultSet = statement.executeQuery();
		) {
			Set<Integer> processedCourses = new HashSet<>();
			Course currentCourse = null;
			while (resultSet.next()){
				if (!processedCourses.contains(resultSet.getInt("id"))){
					currentCourse = courseRowMapper.apply(resultSet);
					courses.add(currentCourse);
				}
				else {
					Student student = studentRowMapper.apply(resultSet);
					student.getCourses().add(currentCourse);
					currentCourse.getStudents().add(student);

					Instructor instructor = instructorRowMapper.apply(resultSet);
					instructor.getCourses().add(currentCourse);
					currentCourse.setInstructor(instructor);
				}
				processedCourses.add(currentCourse.getId());
			}
		} catch (SQLException e) {
			throw new IllegalArgumentException(e);
		}
		return courses;
	}

	@Override
	public Optional<Course> findById(Integer id) {
		try (Connection connection = dataSource.getConnection();
			 	PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID);) {
			statement.setInt(1, id);
			try (ResultSet resultSet = statement.executeQuery()){
				if (resultSet.next()){
					Course course = courseRowMapper.apply(resultSet);
					Instructor instructor = instructorRowMapper.apply(resultSet);
					instructor.getCourses().add(course);
					course.setInstructor(instructor);
					do {
						Student student = studentRowMapper.apply(resultSet);
						student.getCourses().add(course);
						course.getStudents().add(student);
					} while (resultSet.next());
					return Optional.of(course);
				} else {
					return Optional.empty();
				}
			}
		} catch (SQLException e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public List<Course> findByName(String name) {
		List<Course> courses = new ArrayList<>();
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_NAME)){
			statement.setString(1, name);
			try (ResultSet resultSet = statement.executeQuery()){
				Set<Integer> processedCourses = new HashSet<>();
				Course currentCourse = null;
				while (resultSet.next()){
					if (!processedCourses.contains(resultSet.getInt("id"))){
						currentCourse = courseRowMapper.apply(resultSet);
						courses.add(currentCourse);
					}
					else {
						Student student = studentRowMapper.apply(resultSet);
						student.getCourses().add(currentCourse);
						currentCourse.getStudents().add(student);

						Instructor instructor = instructorRowMapper.apply(resultSet);
						instructor.getCourses().add(currentCourse);
						currentCourse.setInstructor(instructor);
					}
					processedCourses.add(currentCourse.getId());
				}
			}
		} catch (SQLException e){
			throw new IllegalArgumentException(e);
		}
		return courses;
	}

	@Override
	public void save(Course course) {
		try (Connection connection = dataSource.getConnection();
			 PreparedStatement statement = connection.prepareStatement(SQL_INSERT);){
			statement.setString(1, course.getName());
			statement.setString(2, LocalDateTimeUtils.dateStringOfLocalDate(course.getStartDate()));
			statement.setString(3, LocalDateTimeUtils.dateStringOfLocalDate(course.getEndDate()));
			statement.setInt(4, course.getInstructor().getId());
			int affectedRows = statement.executeUpdate();
			if (affectedRows != 1) {
				throw new SQLException("Exception in <Save>");
			}
			try (ResultSet keys = statement.getGeneratedKeys()){
				if (keys.next()){
					course.setId(keys.getInt(1));
				}
			}
		} catch (SQLException e){
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public void update(Course course) {
		try (Connection connection = dataSource.getConnection();
			 PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_BY_ID)) {

			statement.setString(1, course.getName());
			statement.setString(2, LocalDateTimeUtils.dateStringOfLocalDate(course.getStartDate()));
			statement.setString(3, LocalDateTimeUtils.dateStringOfLocalDate(course.getEndDate()));
			statement.setInt(4, course.getInstructor().getId());

			int affectedRows = statement.executeUpdate();

			if (affectedRows != 1) {
				throw new SQLException("Exception in <Update>");
			}

		} catch (SQLException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
