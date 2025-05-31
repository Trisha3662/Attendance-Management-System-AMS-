package attendance.reports;

import attendance.models.Student;
import attendance.models.Teacher;
import java.util.Date;
import java.util.List;

public class AttendanceReport {
    public String generateDailyReport(Date date, List<Student> students, List<Teacher> teachers) {
        StringBuilder report = new StringBuilder();
        report.append("Daily Attendance Report - ").append(date).append("\n\n");
        
        report.append("Students Present:\n");
        for (Student student : students) {
            report.append(String.format("ID: %d, Name: %s\n", student.getId(), student.getName()));
        }
        
        report.append("\nTeachers Present:\n");
        for (Teacher teacher : teachers) {
            report.append(String.format("ID: %d, Name: %s\n", teacher.getId(), teacher.getName()));
        }
        
        return report.toString();        //Converts StringBuilder to a String
    }
}