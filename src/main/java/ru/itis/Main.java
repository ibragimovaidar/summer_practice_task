package ru.itis;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.itis.models.Course;
import ru.itis.models.Lesson;
import ru.itis.repository.CoursesRepository;
import ru.itis.repository.LessonsRepository;
import ru.itis.repository.impl.CoursesRepositoryJDBCImpl;
import ru.itis.repository.impl.LessonsRepositorySpringJDBCImpl;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;


public class Main {

	public static void main(String[] args) {
		Properties properties = new Properties();
		try {
			properties.load(new FileReader("src/main/resources/application.properties"));
		} catch (IOException e){
			throw new IllegalArgumentException(e);
		}

		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDriverClassName(properties.getProperty("db.driver"));
		hikariConfig.setJdbcUrl(properties.getProperty("db.url"));
		hikariConfig.setUsername(properties.getProperty("db.user"));
		hikariConfig.setPassword(properties.getProperty("db.password"));
		hikariConfig.setMaximumPoolSize(Integer.parseInt(properties.getProperty("db.hikari.pool-size")));

		DataSource dataSource = new HikariDataSource(hikariConfig);

		CoursesRepository coursesRepository = new CoursesRepositoryJDBCImpl(dataSource);
		LessonsRepository lessonsRepository = new LessonsRepositorySpringJDBCImpl(dataSource);
		System.out.println(coursesRepository.findById(1));
		System.out.println(lessonsRepository.findById(1));
	}


}
