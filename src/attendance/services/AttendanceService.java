package attendance.services;          //manage student attendance records

import attendance.models.Student;
import java.util.*;

public class AttendanceService {
    private Map<String, List<Student>> attendanceRecords;

    public AttendanceService() {
        this.attendanceRecords = new HashMap<>();       //Initializes an empty HashMap to store attendance records.
    }

    public void markAttendance(Student student, String className) {
        attendanceRecords.computeIfAbsent(className, k -> new ArrayList<>()).add(student);
    }

    public Map<String, List<Student>> getAttendanceRecords() {
        return new HashMap<>(attendanceRecords);                  // Returns a copy of the attendance records.
    }

    public List<Student> getAttendedStudents() {
        List<Student> allAttendedStudents = new ArrayList<>();
        attendanceRecords.values().forEach(allAttendedStudents::addAll);
        return allAttendedStudents;
    }
}