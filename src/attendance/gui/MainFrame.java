package attendance.gui;

import javax.swing.*;  //Includes elements like JFrame, JPanel, JButton, JTable, JLabel, JTextField, etc.
import javax.swing.table.DefaultTableModel;
import java.awt.*;         //Used for GUI layout management and color styling.
import java.util.*;
import attendance.models.*;
import attendance.services.*;
import attendance.reports.AttendanceReport;
import attendance.utils.DateUtils;
import java.util.List;

public class MainFrame extends JFrame {
    private Map<String, List<Student>> classMap;      //Stores a mapping of class names to lists of students.
    private Map<String, List<Student>> attendanceMap;
    private List<Integer> usedIds;      // A list of unique student IDs.
    private List<Teacher> teacherList;
    private TeacherAttendanceService teacherAttendanceService;
    private AttendanceService attendanceService;
    private AttendanceReport attendanceReport;
    private JTextArea attendanceTextArea;

    public MainFrame() {
        initializeComponents(); //Initializes data structures and services.
        setupUI();       //Builds the user interface.
    }

    private void initializeComponents() {
        classMap = new HashMap<>();
        attendanceMap = new HashMap<>();
        usedIds = new ArrayList<>();
        teacherList = new ArrayList<>();
        teacherAttendanceService = new TeacherAttendanceService();
        attendanceService = new AttendanceService();
        attendanceReport = new AttendanceReport();

        setTitle("Attendance Management System");
        setSize(800, 600);        // Defines window size.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);      //Ensures program closes properly.
        setLocationRelativeTo(null);       //Centers the window.
    }

    private void setupUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTabbedPane tabbedPane = new JTabbedPane();
        attendanceTextArea = new JTextArea(10, 40);
        attendanceTextArea.setEditable(false);

        // Create and add panels
        tabbedPane.addTab("Student Management", createStudentPanel());
        tabbedPane.addTab("Teacher Management", new TeacherPanel(teacherAttendanceService, attendanceTextArea));
        tabbedPane.addTab("Attendance", new AttendancePanel(attendanceService, teacherAttendanceService));

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(new JScrollPane(attendanceTextArea), BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createStudentPanel() {   //Uses GridLayout to arrange buttons vertically.
        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10)); // Changed to GridLayout for vertical arrangement
        
        panel.add(createButton("Select Branch", e -> selectBranch()));
        panel.add(createButton("Show Classes", e -> showClasses()));
        panel.add(createButton("Attended Students", e -> showAttendedStudents()));
        panel.add(createButton("Absent Students", e -> showAbsentStudents()));
        
        return panel;
    }
    //Creating Buttons
    private JButton createButton(String text, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        button.setPreferredSize(new Dimension(150, 40)); // Increased button height for better visibility
        return button;
    }

    private void selectBranch() {    //Displays a dialog box for branch selection.
        String[] branches = {"CSE", "ECE", "Mechanical", "CYS"};
        String selectedBranch = (String) JOptionPane.showInputDialog(
            this,
            "Select Branch:",
            "Branch Selection",
            JOptionPane.QUESTION_MESSAGE,
            null,
            branches,
            branches[0]
        );

        if (selectedBranch != null) {
            addClass(selectedBranch);
        }
    }

    private void addClass(String branch) {     //Allows users to create a new class for a selected branch 
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField classNameField = new JTextField();  //Text fields allow the user to enter the class name and number of students.
        JTextField numStudentsField = new JTextField();

        panel.add(new JLabel("Enter Class Name:"));
        panel.add(classNameField);
        panel.add(new JLabel("Enter Number of Students:"));
        panel.add(numStudentsField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Class to " + branch, JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            String className = classNameField.getText().trim();
            if (className.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Class name cannot be empty!");
                return;
            }

            try {
                int numStudents = Integer.parseInt(numStudentsField.getText().trim());
                if (numStudents <= 0) {
                    JOptionPane.showMessageDialog(this, "⚠️ Number of students must be positive!");
                    return;
                }

                List<Student> students = new ArrayList<>();
                for (int i = 0; i < numStudents; i++) {
                    addStudentToClass(students);
                }
                
                String fullClassName = branch + " - " + className;
                classMap.put(fullClassName, students);
                attendanceMap.put(fullClassName, new ArrayList<>());
                JOptionPane.showMessageDialog(this, "✅ Class " + fullClassName + " added successfully!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "⚠️ Invalid number of students!");
            }
        }
    }

    private void addStudentToClass(List<Student> students) {
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField nameField = new JTextField();
        JTextField idField = new JTextField();

        panel.add(new JLabel("Enter Student Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Enter Student ID:"));
        panel.add(idField);

        while (true) {
            int result = JOptionPane.showConfirmDialog(this, panel, "Add Student", JOptionPane.OK_CANCEL_OPTION);

            if (result != JOptionPane.OK_OPTION) {
                return;
            }

            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Please enter a name!");
                continue;
            }

            try {
                int id = Integer.parseInt(idField.getText().trim());
                
                if (usedIds.contains(id)) {
                    JOptionPane.showMessageDialog(this, "⚠️ ID already exists!");
                    continue;
                }

                usedIds.add(id);
                students.add(new Student(name, id));
                JOptionPane.showMessageDialog(this, "✅ Student added successfully!");
                break;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "⚠️ Invalid ID! Enter a number.");
            }
        }
    }

    private void showClasses() {
        if (classMap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ No classes available.");
            return;
        }

        String[] classNames = classMap.keySet().toArray(new String[0]);
        Arrays.sort(classNames);
        
        String selectedClass = (String) JOptionPane.showInputDialog(
            this,
            "Select Class:",
            "Class Selection",
            JOptionPane.QUESTION_MESSAGE,
            null,
            classNames,
            classNames[0]
        );

        if (selectedClass != null) {
            List<Student> students = classMap.get(selectedClass);
            displayStudentTable(selectedClass, students, false);
        }
    }

    private void displayStudentTable(String className, List<Student> students, boolean isAttended) {
        String[] columnNames = {"ID", "Name"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Student student : students) {
            model.addRow(new Object[]{student.getId(), student.getName()});
        }

        JTable table = new JTable(model);
        table.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titleLabel = new JLabel("Class: " + className, JLabel.CENTER);
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 14));
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        if (!isAttended) {
            JButton markAttendanceButton = new JButton("Mark Attendance");
            markAttendanceButton.addActionListener(e -> markAttendance(className));
            panel.add(markAttendanceButton, BorderLayout.SOUTH);
        }

        JOptionPane.showMessageDialog(this, panel, 
            isAttended ? "Attended Students" : "Students List", 
            JOptionPane.PLAIN_MESSAGE);
    }

    private void markAttendance(String className) {
        List<Student> students = classMap.get(className);
        List<Student> presentStudents = attendanceMap.get(className);

        JPanel panel = new JPanel(new GridLayout(1, 2, 5, 5)); // Removed password field
        JTextField idField = new JTextField();

        panel.add(new JLabel("Student ID:"));
        panel.add(idField);

        int result = JOptionPane.showConfirmDialog(this, panel, 
            "Student Login", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int id = Integer.parseInt(idField.getText().trim());

                for (Student student : students) {
                    if (student.getId() == id) { // Removed password check
                        if (!presentStudents.contains(student)) {
                            presentStudents.add(student);
                            attendanceService.markAttendance(student, className); // Pass className
                            JOptionPane.showMessageDialog(this, 
                                "✅ " + student.getName() + " marked present.");
                        } else {
                            JOptionPane.showMessageDialog(this, 
                                "⚠️ Student already marked present.");
                        }
                        return;
                    }
                }
                JOptionPane.showMessageDialog(this, "⚠️ Invalid ID!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "⚠️ Please enter a valid ID number!");
            }
        }
    }

    private void showAttendedStudents() {
        if (attendanceMap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ No attendance records available.");
            return;
        }

        String[] classNames = attendanceMap.keySet().toArray(new String[0]);
        Arrays.sort(classNames);

        String selectedClass = (String) JOptionPane.showInputDialog(
            this,
            "Select Class:",
            "Attended Students",
            JOptionPane.QUESTION_MESSAGE,
            null,
            classNames,
            classNames[0]
        );

        if (selectedClass != null) {
            List<Student> attendedStudents = attendanceMap.get(selectedClass);
            displayStudentTable(selectedClass, attendedStudents, true);
        }
    }

    private void showAbsentStudents() {
        if (classMap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ No classes available.");
            return;
        }

        String[] classNames = classMap.keySet().toArray(new String[0]);
        Arrays.sort(classNames);

        String selectedClass = (String) JOptionPane.showInputDialog(
            this,
            "Select Class:",
            "Absent Students",
            JOptionPane.QUESTION_MESSAGE,
            null,
            classNames,
            classNames[0]
        );

        if (selectedClass != null) {
            List<Student> allStudents = classMap.get(selectedClass);
            List<Student> attendedStudents = attendanceMap.get(selectedClass);
            List<Student> absentStudents = new ArrayList<>(allStudents);

            if (attendedStudents != null) {
                absentStudents.removeAll(attendedStudents);
            }

            displayStudentTable(selectedClass, absentStudents, false);
        }
    }
}