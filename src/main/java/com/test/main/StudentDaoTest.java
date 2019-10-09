package com.test.main;




import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.test.dao.StudentDao;
import com.test.exception.ApplictionException;
import com.test.model.Course;
import com.test.model.Student;
import com.test.model.StudentCourse;





@ComponentScan("com.test")
@SpringBootApplication
public class StudentDaoTest implements CommandLineRunner{

	
	@Autowired
	private StudentDao studentDao;
	public static void main(String[] args) {
		try {
			SpringApplication.run(StudentDaoTest.class, args);
		}catch(Exception e) {
			System.exit(1);
		}
		
		
	}
	
	 @Override
	 public void run(String... strings) throws ApplictionException {
		 
		 Student student=new Student(1, "ABC");
		 Course course1= new Course(1, "ABC");
		 Course course2= new Course(1, "ABC");
         Set<StudentCourse>  studentCourses=new HashSet<StudentCourse>();
         studentCourses.add(new StudentCourse(1,1, 0, new java.util.Date(),null ));  //stId, courseId, courseMarks(set to 0), courseStartDate, courseEndDate
         studentCourses.add(new StudentCourse(2,1, 0, new java.util.Date(),null ));
         
 		 studentDao.addStudentAlongWthCourses(student,  studentCourses);         

		
    }

}



