package com.test.model;

import java.util.HashSet;
import java.util.Set;

public class Course {
	private String courseName;
	private Set<StudentCourse> courses=new HashSet<StudentCourse>();
	
	public Course(long courseId, String courseName) {
		super();
		this.courseId = courseId;
		this.courseName = courseName;
	}
	private long courseId;
	public long getCourseId() {
		return courseId;
	}
	public void setCourseId(long courseId) {
		this.courseId = courseId;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public Set<StudentCourse> getCourses() {
		return courses;
	}
	
	
	public void setCourses(Set<StudentCourse> courses) {
		this.courses = courses;
	}
	
	public void addStudentCourse(StudentCourse course) {
		courses.add(course);
	}
	
	
	public boolean equlas(Course  st) {
		return this.getCourseId()==st.getCourseId();
	}
	
	public int hashCode() {
		return 11*Long.valueOf(this.getCourseId()).hashCode();
	}

}
