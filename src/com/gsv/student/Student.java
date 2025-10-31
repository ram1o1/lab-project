package com.gsv.student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Arrays;
import java.util.Vector;

import com.gsv.utils.Register; // Import the utility class

public class Student {
    // Database constants copied from StudentSignUp.java for internal use
    public static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String DB_URL = "jdbc:mysql://localhost:3306/gsv";
    public static final String DB_USER = "root";
    public static final String DB_PASS = "4041";

    private Integer studentId;
    private String studentName;
    private String rollNo;
    private String gender;
    private Long phNo;
    private Integer semester;
    private String registeredCourses; // To store comma-separated course codes

    private JFrame frame;
    private JPanel contentPanel; // Panel for dynamic content display

    private static Connection getConnection() throws SQLException {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found.");
            throw new SQLException(e);
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }
    
    public Student(Integer student_id, JFrame previousFrame) {
        this.studentId = student_id;
        fetchStudentData();
        createStudentDashboard(previousFrame);
    }

    // Made public so it can be called to refresh data after registration/drop
    public void fetchStudentData() {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        // SQL to select all fields including the new 'courses' column
        String sql_query = "SELECT student_name, roll_no, gender, ph_No, semester, courses FROM StudentData WHERE student_id = ?";

        try {
            con = getConnection();
            stmt = con.prepareStatement(sql_query);
            stmt.setInt(1, this.studentId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                // Populate student object with data
                this.studentName = rs.getString("student_name");
                this.rollNo = rs.getString("roll_no");
                this.gender = rs.getString("gender");
                this.phNo = rs.getLong("ph_No");
                this.semester = rs.getInt("semester");
                // Retrieve the new courses field
                this.registeredCourses = rs.getString("courses"); 
                if (this.registeredCourses == null) {
                    this.registeredCourses = ""; // Initialize to empty string if NULL
                }
            } else {
                 JOptionPane.showMessageDialog(null, "Student data not found in database.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error: Could not fetch student data.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void createStudentDashboard(JFrame previousFrame) {
        frame = new JFrame("Student Dashboard - " + (studentName != null ? studentName : "User"));
        frame.setSize(1000, 650); 
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // --- TOP PANEL (Header, Welcome, Navigation Buttons) ---
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS)); 

        // 1. Welcome and Account/Logout row
        JPanel headerRow = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome " + (studentName != null ? studentName.toUpperCase() : "STUDENT") + "!", SwingConstants.LEFT);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        headerRow.add(welcomeLabel, BorderLayout.WEST);

        JPanel utilityButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        JButton accountButton = new JButton("Account Details");
        accountButton.addActionListener(e -> showAccountDetails());
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            frame.dispose();
            if (previousFrame != null) {
                previousFrame.setVisible(true); 
            }
        });
        utilityButtons.add(accountButton);
        utilityButtons.add(logoutButton);
        headerRow.add(utilityButtons, BorderLayout.EAST);
        headerRow.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10)); 

        // 2. Main Navigation Buttons row
        JPanel navRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        navRow.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10)); 

        JButton scheduleButton = new JButton("Schedule");
        navRow.add(scheduleButton);

        JButton registeredButton = new JButton("Registered Courses");
        registeredButton.addActionListener(e -> showRegisteredCourses());
        navRow.add(registeredButton);

        JButton availableCoursesButton = new JButton("Available Courses");
        availableCoursesButton.addActionListener(e -> showAvailableCourses()); 
        navRow.add(availableCoursesButton);

        JButton academicProgressButton = new JButton("Academic Progress");
        navRow.add(academicProgressButton);

        JButton complaintsButton = new JButton("Complaints");
        navRow.add(complaintsButton);


        topPanel.add(headerRow);
        topPanel.add(navRow);
        frame.add(topPanel, BorderLayout.NORTH);

        // --- CENTER PANEL (Dynamic Content Area) ---
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        // Initial default content
        showWelcomeMessage(); 
        
        frame.add(contentPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
    
    // Helper to switch content in the center panel
    private void setContent(Component component) {
        contentPanel.removeAll();
        contentPanel.add(component, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showWelcomeMessage() {
        JTextArea welcomeText = new JTextArea("\n\n\tSelect an option from the navigation bar above.\n\n\tYour current semester is: " + (semester != null ? semester : "N/A"));
        welcomeText.setFont(new Font("SansSerif", Font.PLAIN, 20));
        welcomeText.setEditable(false);
        setContent(new JScrollPane(welcomeText));
    }

    private void showAccountDetails() {
        JPanel detailPanel = new JPanel(new GridLayout(7, 2, 10, 10)); // 7 rows now for 'courses'
        detailPanel.setBorder(BorderFactory.createEmptyBorder(30, 150, 30, 150));
        
        Font labelFont = new Font("SansSerif", Font.BOLD, 14);
        Font dataFont = new Font("SansSerif", Font.PLAIN, 14);

        autoAddDetail(detailPanel, "Student ID:", studentId != null ? String.valueOf(studentId) : "N/A", labelFont, dataFont);
        autoAddDetail(detailPanel, "Name:", studentName != null ? studentName : "N/A", labelFont, dataFont);
        autoAddDetail(detailPanel, "Roll No:", rollNo != null ? rollNo : "N/A", labelFont, dataFont);
        autoAddDetail(detailPanel, "Gender:", gender != null ? gender : "N/A", labelFont, dataFont);
        autoAddDetail(detailPanel, "Phone No:", phNo != null ? String.valueOf(phNo) : "N/A", labelFont, dataFont);
        autoAddDetail(detailPanel, "Semester:", semester != null ? String.valueOf(semester) : "N/A", labelFont, dataFont);
        autoAddDetail(detailPanel, "Courses Opted:", registeredCourses != null ? registeredCourses : "None", labelFont, dataFont);
        
        setContent(detailPanel);
    }

    private void autoAddDetail(JPanel panel, String labelText, String dataText, Font labelFont, Font dataFont) {
        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        panel.add(label);

        JLabel data = new JLabel(dataText);
        data.setFont(dataFont);
        panel.add(data);
    }

    // --- Core logic for displaying available courses ---
    private void showAvailableCourses() {
        if (this.semester == null || this.semester < 1 || this.semester > 8) {
            JTextArea errorText = new JTextArea("\n\n\tError: Semester number is invalid (" + this.semester + "). Cannot fetch courses.");
            errorText.setFont(new Font("SansSerif", Font.BOLD, 18));
            errorText.setEditable(false);
            setContent(new JScrollPane(errorText));
            return;
        }

        String tableName = "Sem" + this.semester + "Courses";
        
        JPanel listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

        JLabel header = new JLabel("Available Courses for Semester " + this.semester, SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 18));
        listContainer.add(header);
        listContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        final String[] registeredCodes = registeredCourses != null ? 
                                         registeredCourses.toUpperCase().split(",") : 
                                         new String[]{};
        
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            stmt = con.createStatement();
            String sql_query = "SELECT * FROM " + tableName; 
            rs = stmt.executeQuery(sql_query);
            
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            Vector<String> columnNames = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i).toUpperCase().replace("_", " "));
            }
            
            boolean hasCourses = false;
            
            while (rs.next()) {
                hasCourses = true;
                final String courseCode = rs.getString(1).trim(); 
                
                boolean isRegistered = Arrays.stream(registeredCodes)
                                            .anyMatch(code -> code.trim().equals(courseCode.toUpperCase()));

                JPanel courseRow = new JPanel(new BorderLayout(20, 0));
                courseRow.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
                courseRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80)); 
                courseRow.setAlignmentX(Component.CENTER_ALIGNMENT);

                StringBuilder details = new StringBuilder("<html><b>");
                details.append(columnNames.get(0)).append(": ").append(courseCode).append(" - ");
                if (columnCount > 1) {
                    details.append(rs.getString(2));
                }
                details.append("</b><br>");
                
                for (int i = 3; i <= columnCount; i++) { 
                    details.append(columnNames.get(i-1)).append(": ").append(rs.getObject(i)).append("; ");
                }
                details.append("</html>");
                
                JLabel detailsLabel = new JLabel(details.toString());
                courseRow.add(detailsLabel, BorderLayout.CENTER);

                JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                if (isRegistered) {
                    JLabel statusLabel = new JLabel("REGISTERED");
                    statusLabel.setForeground(Color.GREEN.darker());
                    statusLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
                    buttonWrapper.add(statusLabel);
                } else {
                    JButton registerBtn = new JButton("Register");
                    registerBtn.addActionListener(e -> registerCourseAction(courseCode)); 
                    buttonWrapper.add(registerBtn);
                }

                courseRow.add(buttonWrapper, BorderLayout.EAST);
                listContainer.add(courseRow);
                listContainer.add(Box.createRigidArea(new Dimension(0, 5))); 
            }
            
            if (!hasCourses) {
                JLabel noCourses = new JLabel("No courses found for Semester " + this.semester, SwingConstants.CENTER);
                noCourses.setFont(new Font("SansSerif", Font.PLAIN, 16));
                listContainer.add(noCourses);
            }

            JScrollPane scrollPane = new JScrollPane(listContainer);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16); 
            
            setContent(scrollPane);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JTextArea errorText = new JTextArea("\n\n\tDatabase Error:\n\tCould not load courses from table: " + tableName + ".\n\tPlease ensure the table exists and the database connection is active.");
            errorText.setFont(new Font("SansSerif", Font.BOLD, 18));
            errorText.setEditable(false);
            setContent(new JScrollPane(errorText));
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void registerCourseAction(String courseCode) {
        String courseCodeUpper = courseCode.toUpperCase().trim();
        
        // 1. Check if already registered (Local check)
        if (registeredCourses != null) {
            String[] currentCourses = registeredCourses.toUpperCase().split(",");
            if (Arrays.stream(currentCourses).anyMatch(code -> code.trim().equals(courseCodeUpper))) {
                JOptionPane.showMessageDialog(frame, courseCode + " is already registered.", "Registration Failed", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        // 2. Prepare the new course string
        String newCourses;
        if (registeredCourses == null || registeredCourses.isEmpty()) {
            newCourses = courseCode;
        } else {
            newCourses = registeredCourses + "," + courseCode;
        }

        // 3. Call the utility function to update the database
        if (Register.registerStudentCourse(this.studentId, newCourses)) {
            // 4. Update local state and refresh UI on success
            fetchStudentData(); // Refresh all student data, including the registeredCourses string
            JOptionPane.showMessageDialog(frame, courseCode + " registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            showAvailableCourses(); // Refresh the available courses view
        } else {
            JOptionPane.showMessageDialog(frame, "Failed to update database for " + courseCode, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // --- NEW: Wrapper method to handle course dropping logic ---
    private void dropCourseAction(String courseCode) {
        String courseCodeUpper = courseCode.toUpperCase().trim();
        
        // Confirmation dialog
        int response = JOptionPane.showConfirmDialog(frame, 
            "Are you sure you want to drop the course " + courseCode + "? This action cannot be undone.", 
            "Confirm Course Drop", JOptionPane.YES_NO_OPTION);
        
        if (response != JOptionPane.YES_OPTION) {
            return;
        }

        // 1. Get current courses and filter out the one to drop
        String[] currentCourses = registeredCourses.split(",");
        
        // Use StringBuilder to reconstruct the new comma-separated list
        StringBuilder newCoursesBuilder = new StringBuilder();
        boolean first = true;
        boolean courseFound = false;

        for (String course : currentCourses) {
            String trimmedCourse = course.trim().toUpperCase();
            
            if (trimmedCourse.isEmpty()) continue;

            if (trimmedCourse.equals(courseCodeUpper)) {
                courseFound = true; // Mark as found but skip adding it
            } else {
                if (!first) {
                    newCoursesBuilder.append(",");
                }
                newCoursesBuilder.append(trimmedCourse);
                first = false;
            }
        }

        if (!courseFound) {
            JOptionPane.showMessageDialog(frame, courseCode + " was not found in registered list.", "Drop Failed", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String newCourses = newCoursesBuilder.toString();
        
        // 2. Call the utility function to update the database
        if (Register.dropStudentCourse(this.studentId, newCourses)) {
            // 3. Update local state and refresh UI on success
            fetchStudentData(); 
            JOptionPane.showMessageDialog(frame, courseCode + " dropped successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            showRegisteredCourses(); // Refresh the registered courses view
        } else {
            JOptionPane.showMessageDialog(frame, "Failed to update database for dropping " + courseCode, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    // --- MODIFIED FUNCTION: showRegisteredCourses to display full details and Drop button ---
    private void showRegisteredCourses() {
        if (this.semester == null || this.semester < 1 || this.semester > 8) {
            JTextArea errorText = new JTextArea("\n\n\tError: Semester number is invalid (" + this.semester + "). Cannot fetch course details.");
            errorText.setFont(new Font("SansSerif", Font.BOLD, 18));
            errorText.setEditable(false);
            setContent(new JScrollPane(errorText));
            return;
        }

        JPanel listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

        JLabel header = new JLabel("Your Registered Courses (Semester " + (semester != null ? semester : "N/A") + ")", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 18));
        listContainer.add(header);
        listContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        if (registeredCourses == null || registeredCourses.trim().isEmpty()) {
            JLabel noCourses = new JLabel("You are not currently registered for any courses.", SwingConstants.CENTER);
            noCourses.setFont(new Font("SansSerif", Font.PLAIN, 16));
            listContainer.add(noCourses);
            listContainer.add(Box.createVerticalGlue()); 
        } else {
            String[] courses = registeredCourses.split(",");
            
            Connection con = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
                con = getConnection();
                String tableName = "Sem" + this.semester + "Courses";
                
                // Query to get metadata (column names)
                String meta_sql = "SELECT * FROM " + tableName + " WHERE 1=0"; 
                Statement metaStmt = con.createStatement();
                ResultSet metaRs = metaStmt.executeQuery(meta_sql);
                ResultSetMetaData metaData = metaRs.getMetaData();
                String courseCodeColumnName = metaData.getColumnName(1); // Assumes first column is the course code
                metaRs.close();
                metaStmt.close();
                
                String final_query = "SELECT * FROM " + tableName + " WHERE " + courseCodeColumnName + " = ?";
                
                // Get column names for building details string
                int columnCount = metaData.getColumnCount();
                Vector<String> columnNames = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    columnNames.add(metaData.getColumnName(i).toUpperCase().replace("_", " "));
                }
                
                for (String courseCode : courses) {
                    final String code = courseCode.trim();
                    if (code.isEmpty()) continue;
                    
                    // --- 1. Fetch Course Details from DB ---
                    stmt = con.prepareStatement(final_query);
                    stmt.setString(1, code);
                    rs = stmt.executeQuery();

                    if (rs.next()) {
                        
                        // --- 2. Build Detailed HTML String ---
                        JPanel courseRow = new JPanel(new BorderLayout(20, 0));
                        courseRow.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(Color.DARK_GRAY, 1),
                            BorderFactory.createEmptyBorder(10, 10, 10, 10)
                        ));
                        courseRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80)); 
                        courseRow.setAlignmentX(Component.CENTER_ALIGNMENT);

                        StringBuilder details = new StringBuilder("<html><b>");
                        // Course Code and Course Name (assuming second column is Name)
                        details.append(columnNames.get(0)).append(": ").append(rs.getString(1)).append(" - ");
                        if (columnCount > 1) {
                            details.append(rs.getString(2));
                        }
                        details.append("</b><br>");
                        
                        // Add remaining details
                        for (int i = 3; i <= columnCount; i++) { 
                            details.append(columnNames.get(i-1)).append(": ").append(rs.getObject(i)).append("; ");
                        }
                        details.append("</html>");
                        
                        JLabel detailsLabel = new JLabel(details.toString());
                        courseRow.add(detailsLabel, BorderLayout.CENTER);
                        
                        // --- 3. Add the Drop Button ---
                        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                        JButton dropBtn = new JButton("Drop");
                        dropBtn.setBackground(new Color(200, 50, 50)); // Reddish color
                        dropBtn.setForeground(Color.WHITE);          
                        
                        // Use the final variable for the action listener
                        dropBtn.addActionListener(e -> dropCourseAction(code));
                        buttonWrapper.add(dropBtn);
                        
                        courseRow.add(buttonWrapper, BorderLayout.EAST);
                        
                        // --- 4. Add to UI ---
                        listContainer.add(courseRow);
                        listContainer.add(Box.createRigidArea(new Dimension(0, 5)));
                    } else {
                         // Course code exists in StudentData but not in SemXCourses
                        JLabel errorLabel = new JLabel("<html><b style='color: red;'>Error: Could not find details for registered course code: " + code + "</b></html>");
                        listContainer.add(errorLabel);
                        listContainer.add(Box.createRigidArea(new Dimension(0, 5)));
                    }
                    
                    rs.close();
                    stmt.close();
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                JLabel errorLabel = new JLabel("<html><b style='color: red;'>Database Error: Could not load course details. Check semester tables.</b></html>");
                listContainer.add(errorLabel);
            } finally {
                try {
                    if (rs != null) rs.close();
                    if (stmt != null) stmt.close();
                    if (con != null) con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            
            JScrollPane scrollPane = new JScrollPane(listContainer);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16); 
            scrollPane.setBorder(null); 
            setContent(scrollPane);
        }
    }
}