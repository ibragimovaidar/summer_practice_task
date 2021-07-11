package ru.itis.models;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.StringJoiner;

public class Lesson {

	private Integer id;
	private String name;
	private DayOfWeek dayOfWeek;
	private LocalTime time;
	private Course course;

	public Lesson(Integer id, String name, DayOfWeek datOfWeek, LocalTime time) {
		this.id = id;
		this.name = name;
		this.dayOfWeek = datOfWeek;
		this.time = time;
	}

	public Lesson(Integer id, String name, DayOfWeek datOfWeek, LocalTime time, Course course) {
		this.id = id;
		this.name = name;
		this.dayOfWeek = datOfWeek;
		this.time = time;
		this.course = course;
	}

	public Lesson(String name, DayOfWeek dayOfWeek, LocalTime time, Course course) {
		this.name = name;
		this.dayOfWeek = dayOfWeek;
		this.time = time;
		this.course = course;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DayOfWeek getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(DayOfWeek dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", Lesson.class.getSimpleName() + "[", "]")
				.add("name='" + name + "'")
				.add("datOfWeek=" + dayOfWeek)
				.add("time=" + time)
				.add("course=" + course)
				.toString();
	}
}
