package com.gsv.utils;
import javax.swing.*;

import com.gsv.student.Student; // ADDED import for Student dashboard
import com.gsv.utils.LoginAuthentication; // Added for clarity

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;


public class Login {
    public Login(JFrame previousFrame, String userType) {
        JFrame frame;
        JPanel logiPanel, inputPanel, buttonPanel;
        JLabel usernamLabel, passwordlLabel, statusLabel;
        JTextField usernameField, passwordField;
        JButton backButton, logiButton;
        

        frame = new JFrame();
        frame.setSize(600,400);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        frame.setLayout(new BorderLayout());

        logiPanel = new JPanel();
        logiPanel.setLayout(new BorderLayout());

        inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3,2));
        usernamLabel = new JLabel("Username ");
        inputPanel.add(usernamLabel);
        usernameField = new JTextField();
        inputPanel.add(usernameField);
        passwordlLabel = new JLabel("Password ");
        inputPanel.add(passwordlLabel);
        passwordField = new JTextField();
        inputPanel.add(passwordField);
        statusLabel = new JLabel();
        inputPanel.add(statusLabel);
        logiPanel.add(inputPanel, BorderLayout.CENTER);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,2));

        backButton = new JButton("back ");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                previousFrame.setVisible(true);
                frame.dispose();
            }
        });
        buttonPanel.add(backButton);

        logiButton = new JButton("Login ");
        logiButton.addActionListener(e -> {
            String usernameInput = usernameField.getText().trim();
            String passwordInput = passwordField.getText().trim();

            if (usernameInput.isEmpty() || passwordInput.isEmpty()) {
                statusLabel.setText("empty field!!!!");
            }
            else {
                statusLabel.setText(null);
                if (userType.equals("student")) {
                    Integer studentId = LoginAuthentication.getAuthenticatedStudentId(usernameInput, passwordInput);
                    
                    if (studentId == null) {
                        statusLabel.setText("Incorrect Credentials!!");
                    } else {
                        // Success: Close login frame and open student dashboard
                        frame.dispose();
                        new Student(studentId, previousFrame); 
                    }
                }
                else if (userType.equals("prof")) {
                    if (!LoginAuthentication.ProfessorAuthentication(usernameInput, passwordInput)) {
                        statusLabel.setText("Incorrect credentials!!");
                    } else {
                        statusLabel.setText("Login Successful (Professor)");
                        // Add professor dashboard instantiation here
                    }
                } else if (userType.equals("admin")) {
                    if(!LoginAuthentication.AdministratorAuthentication(usernameInput, passwordInput)) {
                        statusLabel.setText("Incorrect Credentials!!");
                    } else {
                        statusLabel.setText("Login Successful (Admin)");
                        // Add admin dashboard instantiation here
                    }
                }
            }

        });
        buttonPanel.add(logiButton);

        logiPanel.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(logiPanel);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                previousFrame.setVisible(true);
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
}