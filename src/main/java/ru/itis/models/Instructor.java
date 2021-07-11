package ru.itis.models;

import java.util.List;
import java.util.StringJoiner;

public class Instructor {

	private Integer id;
	private String firstName;
	private String lastName;
	private Integer yearsOfExperience;
	private List<Course> courses;

	public Instructor(Integer id, String firstName, String lastName, Integer yearsOfExperience) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.yearsOfExperience = yearsOfExperience;
	}

	public Instructor(Integer id, String firstName, String lastName, Integer yearsOfExperience, List<Course> courses) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.yearsOfExperience = yearsOfExperience;
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

	public Integer getYearsOfExperience() {
		return yearsOfExperience;
	}

	public void setYearsOfExperience(Integer yearsOfExperience) {
		this.yearsOfExperience = yearsOfExperience;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", Instructor.class.getSimpleName() + "[", "]")
				.add("firstName='" + firstName + "'")
				.add("lastName='" + lastName + "'")
				.add("yearsOfExperience=" + yearsOfExperience)
				.add("courses=" + courses)
				.toString();
	}
}
