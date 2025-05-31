package attendance.tests;

import attendance.models.Student;
import attendance.models.Teacher;
import attendance.reports.AttendanceReport;
import attendance.services.TeacherAttendanceService;
import attendance.services.AttendanceService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AttendanceSystemTest {
    // Test objects
    private AttendanceReport report;  
    private List<Student> students;    
    private List<Teacher> teachers;    
    private TeacherAttendanceService teacherAttendanceService;   
    private AttendanceService attendanceService;        
    
    // Setup method to initialize test data
    public void setup() {
        report = new AttendanceReport();   //Generates daily attendance reports
        students = new ArrayList<>();      // Stores test student objects.
        teachers = new ArrayList<>();      // Stores test teacher objects.
        teacherAttendanceService = new TeacherAttendanceService();    //Manages teacher attendance records.
        attendanceService = new AttendanceService();     // Manages student attendance records.
        
        // Create test data
//        students.add(new Student("John Smith", 1001));
//        students.add(new Student("Mary Johnson", 1002));
//        
//        teachers.add(new Teacher("Prof. Brown", 2001));
//        teachers.add(new Teacher("Dr. Wilson", 2002));
        
        // Mark attendance
        attendanceService.markAttendance(students.get(0), "default");
        teacherAttendanceService.addTeacher(teachers.get(0));
        teacherAttendanceService.markTeacherAttendance(teachers.get(0).getId());
    }
    
    // Test method for report generation
    public void testReportGeneration() {
        setup();
        System.out.println("Testing Report Generation...");
        
        String report = this.report.generateDailyReport(new Date(), students, teachers);
        
        // Print the generated report
        System.out.println("\nGenerated Report:");
        System.out.println("=================");
        System.out.println(report);
        
        // Verify report contents
        boolean success = true;
        for (Student student : students) {
            if (!report.contains(student.getName())) {
                System.out.println("ERROR: Missing student " + student.getName());
                success = false;
            }
        }
        
        for (Teacher teacher : teachers) {
            if (!report.contains(teacher.getName())) {
                System.out.println("ERROR: Missing teacher " + teacher.getName());
                success = false;
            }
        }
        
        System.out.println("\nTest Result: " + (success ? "PASSED" : "FAILED"));
    }
    
    // Main method to run tests
    public static void main(String[] args) {
        AttendanceSystemTest test = new AttendanceSystemTest();
        test.testReportGeneration();
    }
}