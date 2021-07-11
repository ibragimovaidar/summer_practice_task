package ru.itis.models;

import java.time.LocalDate;
import java.util.List;
import java.util.StringJoiner;

public class Course {

	private Integer id;
	private String name;
	private LocalDate startDate;
	private LocalDate endDate;
	private Instructor instructor;
	private List<Student> students;

	public Course(Integer id, String name, LocalDate startDate, LocalDate endDate) {
		this.id = id;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public Course(Integer id, String name, LocalDate startDate, LocalDate endDate, Instructor instructor, List<Student> students) {
		this.id = id;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.instructor = instructor;
		this.students = students;
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

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public Instructor getInstructor() {
		return instructor;
	}

	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", Course.class.getSimpleName() + "[", "]")
				.add("name='" + name + "'")
				.add("startDate=" + startDate)
				.add("endDate=" + endDate)
				//.add("instructor=" + instructor.getFirstName() + " " + instructor.getLastName())
				.add("students=" + students)
				.toString();
	}
}
