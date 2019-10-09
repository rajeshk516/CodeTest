package com.test.model;

import java.util.Set;

public class Student {
	
	private long studentId;

	private String name;
	private Set<StudentCourse> courses;
	
	public Student(long studentId, String name) {
		super();
		this.studentId = studentId;
		this.name = name;
	}
	public long getStudentId() {
		return studentId;
	}
	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	
	public boolean equlas(Student  st) {
		return this.getStudentId()==st.getStudentId();
	}
	
	public int hashCode() {
		return 11*Long.valueOf(this.getStudentId()).hashCode();
	}

}
