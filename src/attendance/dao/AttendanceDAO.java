package attendance.dao;

import attendance.models.Student;
import attendance.models.Teacher;
import java.util.List;
import java.util.Map;
import java.util.Date;

public interface AttendanceDAO {
    void saveStudentAttendance(int studentId, Date date);
    void saveTeacherAttendance(int teacherId, Date date);
    Map<Integer, List<Date>> getStudentAttendanceHistory(int studentId);
    Map<Integer, List<Date>> getTeacherAttendanceHistory(int teacherId);
}