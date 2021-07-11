package ru.itis.repository.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.itis.models.Course;
import ru.itis.models.Lesson;
import ru.itis.repository.LessonsRepository;
import ru.itis.utils.LocalDateTimeUtils;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LessonsRepositorySpringJDBCImpl implements LessonsRepository {

	private final JdbcTemplate jdbcTemplate;

	//language=SQL
	private final static String SQL_SELECT_BY_ID = "SELECT c.id as course_id, c.name as course_name, * FROM lesson l " +
			"JOIN course c ON l.course_id = c.id " +
			"WHERE l.id = ? ORDER BY l.id";
	//language=SQL
	private final static String SQL_SELECT_BY_NAME = "SELECT c.id as course_id, c.name as course_name, * FROM lesson l " +
			"JOIN course c ON l.course_id = c.id " +
			"WHERE l.name = ? ORDER BY l.id";
	//language=SQL
	private final static String SQL_INSERT = "INSERT INTO lesson(name, day_of_week, time, course_id) VALUES (?,?,?,?)";
	//language=SQL
	private final static String SQL_UPDATE_BY_ID = "UPDATE lesson SET name = ?, day_of_week = ?, time = ?, course_id = ? WHERE lesson.id = ?";

	public LessonsRepositorySpringJDBCImpl(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	private final ResultSetExtractor<List<Lesson>> lessonsResultSetExtractor =  resultSet -> {
		List<Lesson> lessons = new ArrayList<>();
		while (resultSet.next()){
			int id = resultSet.getInt("id");
			String name = resultSet.getString("name");
			DayOfWeek dayOfWeek = DayOfWeek.valueOf(resultSet.getString("day_of_week"));
			LocalTime time = LocalDateTimeUtils.localTimeOfTimeString(resultSet.getString("time"));
			Lesson lesson = new Lesson(id, name, dayOfWeek, time);

			int courseId = resultSet.getInt("course_id");
			String courseName = resultSet.getString("course_name");
			LocalDate startDate = LocalDateTimeUtils.localDateOfDateString(resultSet.getString("start_date"));
			LocalDate endDate = LocalDateTimeUtils.localDateOfDateString(resultSet.getString("end_date"));
			Course course = new Course(courseId, courseName, startDate, endDate);
			lesson.setCourse(course);
			lessons.add(lesson);
		}
		return lessons;
	};

	@Override
	public Optional<Lesson> findById(Integer id) {
		List<Lesson> lessons = jdbcTemplate.query(SQL_SELECT_BY_ID, lessonsResultSetExtractor, id);
		if (lessons == null || lessons.isEmpty()){
			return Optional.empty();
		}
		return Optional.of(lessons.get(0));
	}

	@Override
	public List<Lesson> findByName(String name) {
		return jdbcTemplate.query(SQL_SELECT_BY_NAME, lessonsResultSetExtractor, name);
	}

	@Override
	public void save(Lesson lesson) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(SQL_INSERT);
			statement.setString(1, lesson.getName());
			statement.setString(2, lesson.getDayOfWeek().name());
			statement.setString(3, LocalDateTimeUtils.timeStringOfLocalTime(lesson.getTime()));
			statement.setInt(4, lesson.getCourse().getId());
			return statement;
		}, keyHolder);
		lesson.setId(keyHolder.getKey().intValue());
	}

	@Override
	public void update(Lesson lesson) {
		jdbcTemplate.update(SQL_UPDATE_BY_ID,
				lesson.getName(),
				lesson.getDayOfWeek().name(),
				LocalDateTimeUtils.timeStringOfLocalTime(lesson.getTime()),
				lesson.getCourse().getId(),
				lesson.getId());
	}
}
