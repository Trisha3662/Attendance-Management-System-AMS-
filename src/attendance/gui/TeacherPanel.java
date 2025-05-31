package attendance.gui;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import attendance.services.TeacherAttendanceService;
import attendance.models.Teacher;
import java.util.List;

public class TeacherPanel extends JPanel {
    private JTextField teacherIdField;    //Input field for Teacher ID.
    private JTextField teacherNameField;   //Input field for Teacher Name.
    private JTextArea attendanceTextArea;   //Shared text area to display attendance records.
    private TeacherAttendanceService teacherAttendanceService;  //Manages teacher attendance.
    private List<Teacher> teacherList;      //Stores a list of teachers.

    public TeacherPanel(TeacherAttendanceService teacherAttendanceService, JTextArea attendanceTextArea) {
        this.teacherAttendanceService = teacherAttendanceService;
        this.attendanceTextArea = attendanceTextArea;
        this.teacherList = new ArrayList<>();
        initializeUI();    //build the graphical interface
    }

    private void initializeUI() {   //Uses GridBagLayout for flexible arrangement.
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Input Fields
        addInputField("Teacher ID:", teacherIdField = new JTextField(15), gbc, 0);
        addInputField("Teacher Name:", teacherNameField = new JTextField(15), gbc, 1);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(createButton("Mark Attendance", e -> markAttendance()));
        buttonPanel.add(createButton("Show Records", e -> updateAttendanceRecords()));

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Teacher Attendance"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }

    private void addInputField(String label, JTextField field, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        add(new JLabel(label), gbc);

        gbc.gridx = 1;
        add(field, gbc);
    }

    private JButton createButton(String text, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 30));
        button.addActionListener(listener);
        return button;
    }

    private void markAttendance() {
        try {
            String idText = teacherIdField.getText().trim();
            String name = teacherNameField.getText().trim();

            if (idText.isEmpty() || name.isEmpty()) {
                showError("Please fill in all fields!");
                return;
            }

            int id = Integer.parseInt(idText);       //Converts ID input to integer.
            Teacher teacher = new Teacher(name, id);
            teacherList.add(teacher);    //Creates a Teacher object and adds it to the teacher list.
            teacherAttendanceService.addTeacher(teacher);

            if (teacherAttendanceService.markTeacherAttendance(id)) {
                showSuccess("Attendance marked successfully for " + name);
                clearFields();
                updateAttendanceRecords();
            } else {
                showError("Invalid credentials!");
            }
        } catch (NumberFormatException ex) {
            showError("Please enter a valid Teacher ID!");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, "⚠️ " + message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, "✅ " + message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearFields() {     //Clears teacherIdField and teacherNameField after marking attendance.
        teacherIdField.setText("");
        teacherNameField.setText("");
    }

    private void updateAttendanceRecords() {
        StringBuilder records = new StringBuilder();
        records.append("📋 Teacher Attendance Records\n");
        records.append("=====================================\n\n");
        
        Map<Integer, String> attendanceRecords = teacherAttendanceService.getTeacherAttendanceRecords();
        
        if (attendanceRecords.isEmpty()) {
            records.append("No attendance records found.\n");
        } else {
            attendanceRecords.forEach((id, record) -> {
                records.append(String.format("Teacher ID: %d\n", id));
                records.append(String.format("Details: %s\n", record));
                records.append("-------------------------------------\n");
            });
        }
        
        attendanceTextArea.setText(records.toString());
        attendanceTextArea.setCaretPosition(0);
    }
}