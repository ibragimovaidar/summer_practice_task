package ru.itis.repository;

import ru.itis.models.Lesson;

import java.util.List;
import java.util.Optional;

public interface LessonsRepository {

	Optional<Lesson> findById(Integer id);
	List<Lesson> findByName(String name);
	void save(Lesson lesson);
	void update(Lesson lesson);

}
