package attendance.services;

import attendance.models.Teacher;
import java.util.*;
import java.text.SimpleDateFormat;

public class TeacherAttendanceService {
    private Map<Integer, Teacher> teachers;
    private Map<Integer, Map<Date, String>> teacherAttendanceRecords;

    public TeacherAttendanceService() {
        teachers = new HashMap<>();      //Initializes empty HashMap objects for storing teachers and their attendance records.
        teacherAttendanceRecords = new HashMap<>();
    }

    public void addTeacher(Teacher teacher) {
        teachers.put(teacher.getId(), teacher);
    }

    public boolean markTeacherAttendance(int teacherId) {
        Teacher teacher = teachers.get(teacherId);
        if (teacher != null) {
            teacherAttendanceRecords.computeIfAbsent(teacherId, k -> new HashMap<>())
                .put(new Date(), teacher.getName());
            return true;
        }
        return false;
    }

    public List<Teacher> getAttendedTeachers() {
        List<Teacher> attendedTeachers = new ArrayList<>();
        for (Integer teacherId : teacherAttendanceRecords.keySet()) {
            Teacher teacher = teachers.get(teacherId);
            if (teacher != null) {
                attendedTeachers.add(teacher);
            }
        }
        return attendedTeachers;
    }

    public Map<Integer, String> getTeacherAttendanceRecords() {
        Map<Integer, String> currentRecords = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        for (Map.Entry<Integer, Map<Date, String>> entry : teacherAttendanceRecords.entrySet()) {
            int teacherId = entry.getKey();
            Teacher teacher = teachers.get(teacherId);
            if (teacher != null && !entry.getValue().isEmpty()) {
                String latestDate = dateFormat.format(Collections.max(entry.getValue().keySet()));
                currentRecords.put(teacherId, 
                    String.format("%s (Last attended: %s)", teacher.getName(), latestDate));
            }
        }
        return currentRecords;
    }
}