package attendance.dao;

import java.util.*;

public class DatabaseAttendanceDAO implements AttendanceDAO {
    private Map<Integer, List<Date>> studentAttendanceDb = new HashMap<>();
    private Map<Integer, List<Date>> teacherAttendanceDb = new HashMap<>();

    @Override
    public void saveStudentAttendance(int studentId, Date date) {
        studentAttendanceDb.computeIfAbsent(studentId, k -> new ArrayList<>()).add(date);
    }

    @Override
    public void saveTeacherAttendance(int teacherId, Date date) {
        teacherAttendanceDb.computeIfAbsent(teacherId, k -> new ArrayList<>()).add(date);
    }

    @Override
    public Map<Integer, List<Date>> getStudentAttendanceHistory(int studentId) {
        return Collections.singletonMap(studentId, studentAttendanceDb.getOrDefault(studentId, new ArrayList<>()));
    }

    @Override
    public Map<Integer, List<Date>> getTeacherAttendanceHistory(int teacherId) {
        return Collections.singletonMap(teacherId, teacherAttendanceDb.getOrDefault(teacherId, new ArrayList<>()));
    }
}