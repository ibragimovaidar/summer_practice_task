package ru.itis.repository.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.itis.models.Course;
import ru.itis.models.Instructor;
import ru.itis.models.Student;
import ru.itis.repository.CoursesRepository;
import ru.itis.utils.LocalDateTimeUtils;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.*;

public class CoursesRepositorySpringJDBCImpl implements CoursesRepository {

	private final JdbcTemplate jdbcTemplate;

	//language=SQL
	private static final String SQL_SELECT_ALL = "SELECT i.id AS instructor_id, s.first_name as s_first_name,s.last_name as s_last_name, s.id as student_id, * FROM course c "+
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
	private static final String SQL_INSERT = "INSERT INTO course(name, start_date, end_date, instructor_id) VALUES (?,?,?,?)";

	//language=SQL
	private static final String SQL_UPDATE_BY_ID = "UPDATE course SET name = ?, start_date = ?, end_date = ?, instructor_id = ? WHERE id = ?";

	public CoursesRepositorySpringJDBCImpl(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	private final ResultSetExtractor<List<Course>> courseResultSetExtractor = resultSet -> {
		Map<Integer, Course> idCourseMap = new HashMap<>();
		while (resultSet.next()){
			int id = resultSet.getInt("id");
			Course course = idCourseMap.get(id);
			if (course == null){
				String name = resultSet.getString("name");
				LocalDate startDate = LocalDateTimeUtils.localDateOfDateString(resultSet.getString("start_date"));
				LocalDate endDate = LocalDateTimeUtils.localDateOfDateString(resultSet.getString("end_date"));
				course = new Course(id, name, startDate, endDate);
				course.setStudents(new ArrayList<>());

				int instructorId = resultSet.getInt("instructor_id");
				String instructorFirstName = resultSet.getString("first_name");
				String instructorLastName = resultSet.getString("last_name");
				int yearsOfExperience = resultSet.getInt("years_of_experience");
				Instructor instructor = new Instructor(id, instructorFirstName, instructorLastName, yearsOfExperience);
				instructor.setCourses(new ArrayList<>());
				instructor.getCourses().add(course);
				course.setInstructor(instructor);
				idCourseMap.put(id, course);
			}
			int studentId = resultSet.getInt("student_id");
			String studentFirstName = resultSet.getString("s_first_name");
			String studentLastName = resultSet.getString("s_last_name");
			String group = resultSet.getString("groupp");
			Student student = new Student(studentId, studentFirstName, studentLastName, group);
			student.setCourses(new ArrayList<>());
			student.getCourses().add(course);
			course.getStudents().add(student);
		}
		return new ArrayList<>(idCourseMap.values());
	};

	@Override
	public List<Course> findAll() {
		return jdbcTemplate.query(SQL_SELECT_ALL, courseResultSetExtractor);
	}

	@Override
	public Optional<Course> findById(Integer id) {
		List<Course> courses = jdbcTemplate.query(SQL_SELECT_BY_ID, courseResultSetExtractor, id);
		if (courses != null && !courses.isEmpty()){
			return Optional.empty();
		}
		return Optional.of(courses.get(0));
	}

	@Override
	public List<Course> findByName(String name) {
		return jdbcTemplate.query(SQL_SELECT_BY_ID, courseResultSetExtractor, name);
	}

	@Override
	public void save(Course course) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(SQL_INSERT);
			statement.setString(1, course.getName());
			statement.setString(2, LocalDateTimeUtils.dateStringOfLocalDate(course.getStartDate())); // TODO
			statement.setString(3, LocalDateTimeUtils.dateStringOfLocalDate(course.getEndDate())); // TODO
			statement.setInt(4, course.getInstructor().getId());
			return statement;
		}, keyHolder);
		course.setId(keyHolder.getKey().intValue());
	}

	@Override
	public void update(Course course) {
		jdbcTemplate.update(SQL_UPDATE_BY_ID,
				course.getName(),
				LocalDateTimeUtils.dateStringOfLocalDate(course.getStartDate()),
				LocalDateTimeUtils.dateStringOfLocalDate(course.getEndDate()),
				course.getInstructor().getId(),
				course.getId());
	}
}
