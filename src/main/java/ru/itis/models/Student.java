package ru.itis.models;

import java.util.List;
import java.util.StringJoiner;

public class Student {

	private Integer id;
	private String firstName;
	private String lastName;
	private String group;
	private List<Course> courses;

	public Student(Integer id, String firstName, String lastName, String group) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.group = group;
	}

	public Student(Integer id, String firstName, String lastName, String group, List<Course> courses) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.group = group;
		this.courses = courses;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", Student.class.getSimpleName() + "[", "]")
				.add("firstName='" + firstName + "'")
				.add("lastName='" + lastName + "'")
				.add("group='" + group + "'")
				//.add("courses=" + courses)
				.toString();
	}
}
