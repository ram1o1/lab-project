package com.gsv.utils;
import javax.swing.*;

import com.gsv.student.StudentSignUp;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;


public class UserTypeSelection {
    public UserTypeSelection(JFrame previousFrame, String accountType) {
        JFrame frame;
        JPanel choicePanel;
        JButton studentButton, professorButton, adminButton;

        frame = new JFrame();
        frame.setSize(600, 400);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        frame.setLayout(new BorderLayout());

        choicePanel = new JPanel();
        choicePanel.setLayout(new GridLayout(3,1));

        studentButton = new JButton("Student");
        studentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (accountType == "login") {
                    Login newLogin = new Login(frame, "student");
                }
                else if (accountType == "signup") {
                    StudentSignUp.studentSignUp(frame);
                }
                frame.dispose();
            }
        });
        choicePanel.add(studentButton);

        professorButton = new JButton("Professor");
        professorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (accountType == "login") {
                    Login newLogin = new Login(frame, "prof");
                }
                else if (accountType == "signup") {
                    
                }
                frame.dispose();
            } 
        });
        choicePanel.add(professorButton);

        adminButton = new JButton("Admin");
        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (accountType == "login") {
                    Login newLogin = new Login(frame, "admin");
                }
                else if (accountType == "signup") {

                }
                
                frame.dispose();
            }
        });
        choicePanel.add(adminButton);

        frame.add(choicePanel);

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
