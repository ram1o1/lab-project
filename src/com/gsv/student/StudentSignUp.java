package com.gsv.student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentSignUp {

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

    public static void studentSignUp(JFrame previousFrame) {
        JFrame frame = new JFrame();
        frame.setSize(400,600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);

        JPanel signupPanel = new JPanel();
        signupPanel.setLayout(new GridLayout(9,2));

        JLabel nameLabel = new JLabel("Name ");
        JTextField nameField = new JTextField();
        signupPanel.add(nameLabel);
        signupPanel.add(nameField);

        JLabel emailLabel = new JLabel("Email ID ");
        JTextField emailField = new JTextField();
        signupPanel.add(emailLabel);
        signupPanel.add(emailField);

        JLabel rollNoLabel = new JLabel("Roll No ");
        JTextField rollNoField = new JTextField();
        signupPanel.add(rollNoLabel);
        signupPanel.add(rollNoField);

        JLabel genderLabel = new JLabel("Gender ");
        JTextField genderField = new JTextField();
        signupPanel.add(genderLabel);
        signupPanel.add(genderField);

        JLabel phNoLabel = new JLabel("Ph.No ");
        JTextField phNoField = new JTextField();
        signupPanel.add(phNoLabel);
        signupPanel.add(phNoField);

        JLabel semesterLabel = new JLabel("Semester ");
        JTextField semesterField = new JTextField();
        signupPanel.add(semesterLabel);
        signupPanel.add(semesterField);

        JLabel passwordLabel = new JLabel("Password ");
        JTextField passwordField = new JTextField();
        signupPanel.add(passwordLabel);
        signupPanel.add(passwordField);

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
            String rollNoInput = rollNoField.getText().trim();
            String genderInput = genderField.getText().trim();
            String phNoInput = phNoField.getText().trim();
            String semesterInput = semesterField.getText().trim();
            Long phNoin = null;
            Integer smesterin = null;
            try {
                phNoin = Long.parseLong(phNoInput);
                smesterin = Integer.parseInt(semesterInput);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                statusLabel.setText("invalid phone number or semester");
            }
            String passwordInput = passwordField.getText().trim();

            if (nameInput.isEmpty() || emailInput.isEmpty() || rollNoInput.isEmpty() || genderInput.isEmpty() || phNoInput.isEmpty() || semesterInput.isEmpty() || passwordInput.isEmpty() ) {
                statusLabel.setText("invalid details");
            }
            else {
                if (StudentSignUp.StudentAccountCreation(nameInput, emailInput, rollNoInput, genderInput, phNoin, smesterin , passwordInput)) {
                    JDialog dialog = new JDialog(frame, "acount created sucessfully", true);
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
                }
            }
            
        });
        signupPanel.add(signupButton);

        
        signupPanel.add(statusLabel);

        frame.add(signupPanel);

        frame.setVisible(true);
    }

    public static boolean StudentAccountCreation(String nameInput, String emailInput, String rollNoInput, String genderInput, Long phNoInput, Integer semesterInput, String passwordInput) {
        Connection con = null;
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        boolean isCreated = false;
        ResultSet rs = null;

        String sql_query1 = "insert into StudentLoginData values(null, ?, ?)";
        String sql_query2 = "insert into StudentData values (?, ?, ?, ?, ?, ?)";
        String sql_query3 = "SELECT * FROM StudentLoginData WHERE email_id = ? AND password = ?";
        
        try {
            con = getConnection();

            stmt1 = con.prepareStatement(sql_query1);
            stmt1.setString(1, emailInput);
            stmt1.setString(2, passwordInput);
            stmt1.executeUpdate();

            stmt3 = con.prepareStatement(sql_query3);
            stmt3.setString(1, emailInput);
            stmt3.setString(2, passwordInput);
            rs = stmt3.executeQuery();
            rs.next();
            Integer student_id = rs.getInt(1);

            stmt2 = con.prepareStatement(sql_query2);
            stmt2.setInt(1, student_id);
            stmt2.setString(2, nameInput);
            stmt2.setString(3, rollNoInput);
            stmt2.setString(4, genderInput);
            stmt2.setLong(5, phNoInput);
            stmt2.setInt(6, semesterInput);
            stmt2.executeUpdate();

            isCreated = true;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (stmt1 != null) stmt1.close();
                if (stmt2 != null) stmt2.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return isCreated;
    }

}
    

