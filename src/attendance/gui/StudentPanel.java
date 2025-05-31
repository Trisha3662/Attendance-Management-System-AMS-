package attendance.gui;

import javax.swing.*;
import java.awt.*;
import attendance.services.AttendanceService;
import attendance.models.Student;
import attendance.utils.DateUtils;
import java.util.Date;

public class StudentPanel extends JPanel {
    private JTextField studentIdField;
    private JTextField studentNameField;
    private JTextArea attendanceTextArea;
    private AttendanceService attendanceService;
    private String className;

    public StudentPanel(AttendanceService attendanceService, JTextArea attendanceTextArea, String className) {
        this.attendanceService = attendanceService;
        this.attendanceTextArea = attendanceTextArea;
        this.className = className;
        setupUI();
    }

    private void setupUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addInputField("Student ID:", studentIdField = new JTextField(15), gbc, 0);
        addInputField("Student Name:", studentNameField = new JTextField(15), gbc, 1);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JButton markButton = new JButton("Mark Attendance");
        markButton.addActionListener(e -> markAttendance());
        add(markButton, gbc);

        setBorder(BorderFactory.createTitledBorder("Student Attendance"));
    }

    private void addInputField(String label, JTextField field, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;          
        gbc.gridwidth = 1;
        add(new JLabel(label), gbc);

        gbc.gridx = 1;
        add(field, gbc);
    }
    
    private void markAttendance() {
        try {
            String idText = studentIdField.getText().trim();
            String name = studentNameField.getText().trim();

            if (!name.matches("^[a-zA-Z\\s]+$")) {
                JOptionPane.showMessageDialog(this, 
                    "Name must contain only letters and spaces", 
                    "Invalid Name", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (idText.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Please fill in all fields", 
                    "Missing Information", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(idText);
            Student student = new Student(name, id);
            attendanceService.markAttendance(student, className);
            
            JOptionPane.showMessageDialog(this, 
                "Attendance marked for " + name + "\nDate: " + 
                DateUtils.formatDate(new Date()),
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

            clearFields();
            updateAttendanceRecords();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid Student ID (numbers only)", 
                "Invalid ID", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        studentIdField.setText("");
        studentNameField.setText("");
    }

    private void updateAttendanceRecords() {
        StringBuilder records = new StringBuilder("Student Attendance Records:\n\n");
        var attendedStudents = attendanceService.getAttendedStudents();
        
        if (attendedStudents.isEmpty()) {
            records.append("No attendance records found.");
        } else {
            for (Student student : attendedStudents) {
                records.append(String.format("ID: %d - Name: %s - Date: %s\n",
                    student.getId(),
                    student.getName(),
                    DateUtils.formatDate(new Date())));
            }
        }
        
        attendanceTextArea.setText(records.toString());
    }
}