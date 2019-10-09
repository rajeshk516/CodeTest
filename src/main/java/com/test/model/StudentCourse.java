package com.test.model;

import java.util.Date;

/***
 * 
 * 
 * @author RKAPA
 * 
 * This class contains each course a student is registered to and also details about course ,marks
 * This  POJO class corresponds to join table STUDENT_COURSE
 *
 */

public class StudentCourse {
	
	private long studentId;
	
	private long courseId;
	
	private int coursemarks;
	
	private Date courseStartDate;
	
	private Date courseEndDate;
	
	public StudentCourse(long studentId, long courseid, int courseScore, Date courseStartDate, Date courseEndDate) {
		super();
		this.studentId = studentId;
		this.courseId = courseid;
		this.coursemarks = courseScore;
		this.courseStartDate = courseStartDate;
		this.courseEndDate = courseEndDate;
	}

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	public long getCourseId() {
		return courseId;
	}

	public void setCourseId(long courseid) {
		this.courseId = courseid;
	}

	public int getCourseScore() {
		return coursemarks;
	}

	public void setCourseScore(int courseScore) {
		this.coursemarks = courseScore;
	}

	public Date getCourseStartDate() {
		return courseStartDate;
	}

	public void setCourseStartDate(Date courseStartDate) {
		this.courseStartDate = courseStartDate;
	}

	public Date getCourseEndDate() {
		return courseEndDate;
	}

	public void setCourseEndDate(Date courseEndDate) {
		this.courseEndDate = courseEndDate;
	}



	public boolean equlas(StudentCourse  st) {
		return (this.getCourseId()==st.getCourseId() && this.getStudentId()==st.getStudentId());
	}
	
	public int hashCode() {
		return 11*Long.valueOf(this.getCourseId()).hashCode()+(13*Long.valueOf(this.getStudentId()).hashCode());
	}
	
	
	

}
