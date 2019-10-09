package com.test.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.test.exception.ApplictionException;
import com.test.model.Course;
import com.test.model.Student;
import com.test.model.StudentCourse;


@Component
public class StudentDao {
	
	

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	 private PlatformTransactionManager transactionManager;
	 
   
   /**
    *  2.1 in the assigmnent
    * @param student
    * @param courses
    * 
    * Adds a new with composite primary key of columns  student_id and course_id in JOIN TABLE student_course
    */
	
   
  @Transactional  //Spring declarative transaction management where it handles student and saving courses it commits only if the two operations succeed
 public void addStudentAlongWthCourses(Student student, Set<StudentCourse> newCourses) {

			TransactionStatus status = intializeTransaction("add_student_transaction");
			try 
			{    
				//add student first
		        addStudent(student);
		      //now save courses
			     for(StudentCourse course : newCourses) {
			    	 saveStudentCourse(course.getStudentId(),course.getCourseId(), course.getCourseStartDate());
			    			 
			     }
			}
			catch (Exception ex)
			{    
				transactionManager.rollback(status);  
				throw new ApplictionException(ex.getMessage());
			}
			   transactionManager.commit(status);
	     
		 
	 }


private TransactionStatus intializeTransaction(String transactionName) {
	DefaultTransactionDefinition transDef = new DefaultTransactionDefinition();
	// explicitly set the transaction name
	transDef.setName(transactionName);
	transDef.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
	TransactionStatus status = transactionManager.getTransaction(transDef);
	return status;
}
 
 
	// CourseMarks is not added when new student_course is added. it will be updated later
   public void saveStudentCourse(long studentId, long courseId, Date courseStDate) {
		 
		 String insert_student_course = "INSERT INTO student_course (studentId, courseId , courseStartDate) values (:studentId, :courseId, trunc(sysdate))";				
		 
		 
		 MapSqlParameterSource parameters = new MapSqlParameterSource();
	     parameters.addValue("studentId", studentId);	    
         parameters.addValue("couresId", courseId);	
         
		 namedParameterJdbcTemplate.update(insert_student_course, parameters);
		 
		 //now populate courses		 
	 }
   
   /***
    *   2.2 of Assignment
    * @param studentId
    * 
    * This method deletes student. This operation is two fold
    * 1. It deletes all the courses the student is registerd from student_course table
    * 2.Then it deletes the actual student from student table 
    */
   public void deleteStudent(long studentId) {
		 //first delete all the courses the student is registered to in student_course(JOIN TABLE) table 
	     // then delete the student record in student table
	     // ALTERNATIVE APPROACH - if hibernate is used and one to many mapping is defined from student to courses
	   // and one to many mapping from course to students 
	     //with CASCADE_ALL attribute then if use delete(student) -- hibernate deletes courses the student is registered to 
	     //in student_courses table and then deletes the student record.it automatically takes care of foreign key relations where 
	     // in this case i am managing the relation explicitly. The same applies while saving if student is saved and 
	     // if courses is set on student object hiberantes saves the student record first and then courses in student_course table
	     // 
	   
	    // delete the courses the student is registered to
	   
		TransactionStatus status = intializeTransaction("delete_student_transaction");
		try 
		{    
			String delete_student_course = "delete from student_course where studentId=:studentId";	
			 
			 MapSqlParameterSource parameters = new MapSqlParameterSource();
		     parameters.addValue("studentId", studentId);	    
			 namedParameterJdbcTemplate.update(delete_student_course, parameters);
			 
			   // delete the student now

			 String delete_student = "delete from studente where studentId=:studentId";				
				
			 namedParameterJdbcTemplate.update(delete_student, parameters);
			 
		}
		catch (Exception ex)
		{    
			transactionManager.rollback(status);  
			throw new ApplictionException(ex.getMessage());
		}
		   transactionManager.commit(status);
   }
   
   
   
   /***
    *   2.3 of Assignment
    * @param courseName
    * 
    * This method returns all the students who registered to a given course
    */
   public List<Student> getStudentsRegtoCourse(String courseName) {
		
		 String find_students_reg_to_course = "select student.* from student  inner join student_course inner join course  st_co where "
		 		+ "student.studentId=student_course.studentId and student_course.courseId=course.courseId and course.courseName=:courseName order by student.name";				
		 MapSqlParameterSource parameters = new MapSqlParameterSource();
	     parameters.addValue("courseName", courseName);	
		 List<Student> students=namedParameterJdbcTemplate.query(find_students_reg_to_course, (rs, rowNum) ->
         new Student(
                 rs.getLong("studentId"),
                 rs.getString("name"))
             
          );
		 
         return students;
		
		 
	 }
   
   
   /***
    *   2.4 of Assignment
    * @param studentId, courseId, courseMarks
    * 
    * This method updates the courseMarks a student scores for a course he is registered
    * 
    * Please note there is a column courseMarks in table STUDENT_COURSE(studentId, coursdId, courseMarks)
    */
   public void updateStudentCourseMarks(long studentId, long courseId, int courseMarks) {
		
		 String update_course_marks = "update student_course set courseMarks=:courseMarks, courseEndDate=trunc(sysdate) where studentId=:studentId and courseid=:courseId";				
		 MapSqlParameterSource parameters = new MapSqlParameterSource();
	     parameters.addValue("studentId", studentId);	    
         parameters.addValue("couresId", courseId);	
         parameters.addValue("courseMarks", courseMarks);	

		 int rowsUpdated=namedParameterJdbcTemplate.update(update_course_marks, parameters);
		 if(rowsUpdated!=1) {
			 throw new ApplictionException("No record found in student_course to update");
		 }
		 
   }
		   /***
		    *   2.5 of Assignment
		    * @param courseName, courseId, courseMarks
		    * 
		    * This method finds all the students who are not registered to a course
		    * 
		    * Please note there is a column courseMarks in table STUDENT_COURSE(studentId, coursdId, courseMarks)
		    */
		   public List<Student> findStudentsNotRegToCourse(String courseName) {
				
				 String students_not_reg_to_course = "select * from students where "
				 		+ "NOT EXISTS (select 1 from student_course where courseId = "
				 		+ "(select courseId from course where courseName=: courseName))";
				 MapSqlParameterSource parameters = new MapSqlParameterSource();
		         parameters.addValue("couresName", courseName);	

				 List<Student> studentsNotRegToCourse=namedParameterJdbcTemplate.query(students_not_reg_to_course, parameters, (rs, rowNum) ->
		         new Student(
		                 rs.getLong("studentId"),
		                 rs.getString("name"))
		             
		          );
				 return studentsNotRegToCourse;
			 
		   }
		   
		   
		   /**
			 * 
			 * @param student
			 * 
			 * Adds a new Student to STUDENT Table
			 */
			
			 public void addStudent(Student student) {
				 
				 String insert_student = "INSERT INTO student (studentId, name, email, contact_no) values (:studentId, :name, null, null)";				
				 MapSqlParameterSource parameters = new MapSqlParameterSource();
			     parameters.addValue("studentId", student.getStudentId());	    
		         parameters.addValue("couresId", student.getName());	
				 namedParameterJdbcTemplate.update(insert_student, parameters);		
				 
			 }
			 
			 /**
			  * 
			  * @param course  
			  * 
			  * Adds a new course to COURSE table
			  */
		   public void addNewCourse(Course course) {
				 
				 String insert_course = "INSERT INTO course (courseId, courseName) values (:courseId, :courseName)";				
				 MapSqlParameterSource parameters = new MapSqlParameterSource();
			     parameters.addValue("studentId", course.getCourseId());	    
		         parameters.addValue("couresId", course.getCourseName());			
				 namedParameterJdbcTemplate.update(insert_course, parameters);		
				 
			 }
	

}
