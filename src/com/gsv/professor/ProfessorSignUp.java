package com.gsv.professor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfessorSignUp {

    // Database constants from existing files
    public static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String DB_URL = "jdbc:mysql://localhost:3306/gsv";
    public static final String DB_USER = "root";
    public static final String DB_PASS = "4041";

    private static Connection getConnection() throws SQLException {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found.");
            throw new SQLException(e);
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    public static void professorSignUp(JFrame previousFrame) {
        JFrame frame = new JFrame("Professor Sign Up");
        frame.setSize(400, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);

        // 7 input fields + 1 button row + 1 status row = 9 rows in the grid
        JPanel signupPanel = new JPanel();
        signupPanel.setLayout(new GridLayout(9, 2));

        // --- Input Fields (7) ---
        JLabel nameLabel = new JLabel("Name ");
        JTextField nameField = new JTextField();
        signupPanel.add(nameLabel);
        signupPanel.add(nameField);

        JLabel emailLabel = new JLabel("Email ID ");
        JTextField emailField = new JTextField();
        signupPanel.add(emailLabel);
        signupPanel.add(emailField);

        JLabel genderLabel = new JLabel("Gender ");
        JTextField genderField = new JTextField();
        signupPanel.add(genderLabel);
        signupPanel.add(genderField);

        JLabel phNoLabel = new JLabel("Ph.No ");
        JTextField phNoField = new JTextField();
        signupPanel.add(phNoLabel);
        signupPanel.add(phNoField);

        JLabel designationLabel = new JLabel("Designation ");
        JTextField designationField = new JTextField();
        signupPanel.add(designationLabel);
        signupPanel.add(designationField);

        JLabel subjectsLabel = new JLabel("Subjects (e.g., C, Java) ");
        JTextField subjectsField = new JTextField();
        signupPanel.add(subjectsLabel);
        signupPanel.add(subjectsField);

        JLabel passwordLabel = new JLabel("Password ");
        JTextField passwordField = new JTextField();
        signupPanel.add(passwordLabel);
        signupPanel.add(passwordField);

        // --- Buttons and Status ---
        JButton backButton = new JButton("back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                previousFrame.setVisible(true);
                frame.dispose();
            }
        });
        signupPanel.add(backButton);

        JLabel statusLabel = new JLabel();

        JButton signupButton = new JButton("Signup ");
        signupButton.addActionListener(e -> {
            statusLabel.setText(null);
            String nameInput = nameField.getText().trim();
            String emailInput = emailField.getText().trim();
            String genderInput = genderField.getText().trim();
            String phNoInput = phNoField.getText().trim();
            String designationInput = designationField.getText().trim();
            String subjectsInput = subjectsField.getText().trim();
            String passwordInput = passwordField.getText().trim();
            
            Long phNoin = null;
            
            try {
                // Phone number validation
                if (!phNoInput.isEmpty()) {
                    phNoin = Long.parseLong(phNoInput);
                }
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                statusLabel.setText("invalid phone number");
                return; 
            }

            if (nameInput.isEmpty() || emailInput.isEmpty() || genderInput.isEmpty() || phNoInput.isEmpty() || designationInput.isEmpty() || subjectsInput.isEmpty() || passwordInput.isEmpty()) {
                statusLabel.setText("invalid details: all fields required");
            } else {
                if (ProfessorSignUp.ProfessorAccountCreation(nameInput, emailInput, genderInput, phNoin, designationInput, subjectsInput , passwordInput)) {
                    JDialog dialog = new JDialog(frame, "Account created successfully", true);
                    JButton closeButton = new JButton("close");
                    closeButton.addActionListener(ev -> {
                        dialog.dispose();
                        frame.dispose();
                        previousFrame.setVisible(true);
                    });
                    
                    JPanel dialogBoxButtonPanel = new JPanel();
                    dialogBoxButtonPanel.add(closeButton);
                    dialog.add(dialogBoxButtonPanel);
                    dialog.setSize(300, 150);
                    dialog.setLocationRelativeTo(frame);
                    dialog.setVisible(true);
                } else {
                    statusLabel.setText("Account creation failed (e.g., duplicate email or DB error)");
                }
            }
        });
        signupPanel.add(signupButton);
        
        // Add status label to display messages
        signupPanel.add(statusLabel); 

        frame.add(signupPanel);
        frame.setVisible(true);
    }


    public static boolean ProfessorAccountCreation(String nameInput, String emailInput, String genderInput, Long phNoInput, String designationInput, String subjectsInput, String passwordInput) {
        Connection con = null;
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        boolean isCreated = false;
        ResultSet rs = null;

        // SQL queries
        String sql_query1 = "INSERT INTO ProfessorsLoginData VALUES(null, ?, ?)"; 
        String sql_query2 = "INSERT INTO ProfessorsData VALUES (?, ?, ?, ?, ?, ?)"; 
        String sql_query3 = "SELECT professor_id FROM ProfessorsLoginData WHERE email_id = ? AND password = ?"; // Get the auto-generated ID

        try {
            con = getConnection();
            
            // 1. Insert into Login Table
            stmt1 = con.prepareStatement(sql_query1);
            stmt1.setString(1, emailInput);
            stmt1.setString(2, passwordInput);
            stmt1.executeUpdate();

            // 2. Retrieve the auto-generated Professor ID (professors_id)
            stmt3 = con.prepareStatement(sql_query3);
            stmt3.setString(1, emailInput);
            stmt3.setString(2, passwordInput);
            rs = stmt3.executeQuery();
            
            if (!rs.next()) {
                 // Failsafe check
                 return false;
            }
            Integer professor_id = rs.getInt(1); // Assuming ID is the first column

            // 3. Insert into Data Table
            // Columns: professors_id, professor_name, gender, phone_number, designation, subjects
            stmt2 = con.prepareStatement(sql_query2);
            stmt2.setInt(1, professor_id);
            stmt2.setString(2, nameInput);
            stmt2.setString(3, genderInput);
            stmt2.setLong(4, phNoInput);
            stmt2.setString(5, designationInput);
            stmt2.setString(6, subjectsInput);
            stmt2.executeUpdate();

            isCreated = true;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt1 != null) stmt1.close();
                if (stmt2 != null) stmt2.close();
                if (stmt3 != null) stmt3.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return isCreated;
    }

}