package attendance.gui;

import javax.swing.*; 
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import attendance.services.AttendanceService;
import attendance.services.TeacherAttendanceService;
import attendance.models.Student;
import attendance.models.Teacher;
import java.util.List;

public class AttendancePanel extends JPanel {
    private JTextArea attendanceDisplay;         //Text area for showing attendance reports.
    private AttendanceService attendanceService;        // retrieves the student attendance
    private TeacherAttendanceService teacherAttendanceService;     // retrieves the teacher attendance

    public AttendancePanel(AttendanceService attendanceService, TeacherAttendanceService teacherAttendanceService) {
        this.attendanceService = attendanceService;
        this.teacherAttendanceService = teacherAttendanceService;  //Receives attendance services as parameters.
        setupUI();  //Calls setupUI() to build the graphical user interface
    }

    private void setupUI() {
        setLayout(new BorderLayout(5, 5));    //Uses BorderLayout with spacing.
        attendanceDisplay = new JTextArea(15, 50);
        attendanceDisplay.setEditable(false);
        

        //Adding buttons
        
        JButton showAttendanceButton = new JButton("Show Attendance");
        showAttendanceButton.addActionListener(e -> showAttendance());

        JButton exportReportButton = new JButton("Export Report");
        exportReportButton.addActionListener(e -> exportReport());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(showAttendanceButton);
        buttonPanel.add(exportReportButton);

        add(buttonPanel, BorderLayout.NORTH);
        add(new JScrollPane(attendanceDisplay), BorderLayout.CENTER);
    }

    private void showAttendance() {
        StringBuilder report = new StringBuilder();
        report.append("Daily Attendance Report - ").append(new Date()).append("\n\n");

        report.append("Students Present:\n");
        for (Map.Entry<String, List<Student>> entry : attendanceService.getAttendanceRecords().entrySet()) {
            String className = entry.getKey();
            List<Student> students = entry.getValue();
            for (Student student : students) {
                report.append(String.format("ID: %d, Name: %s, Class: %s\n", student.getId(), student.getName(), className));
            }
        }

        report.append("\nTeachers Present:\n");
        for (Teacher teacher : teacherAttendanceService.getAttendedTeachers()) {
            report.append(String.format("ID: %d, Name: %s\n", teacher.getId(), teacher.getName()));
        }

        attendanceDisplay.setText(report.toString());
    }

    private void exportReport() {
        String reportContent = attendanceDisplay.getText();
        if (reportContent.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ No report to export!", "Export Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Attendance Report");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (FileWriter fileWriter = new FileWriter(fileToSave)) {
                fileWriter.write(reportContent);
                JOptionPane.showMessageDialog(this, "✅ Report exported successfully!", "Export Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "⚠️ Error exporting report: " + e.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}