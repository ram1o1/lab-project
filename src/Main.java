import javax.swing.*;

import com.gsv.utils.Login;
import com.gsv.student.StudentSignUp;
import com.gsv.professor.ProfessorSignUp; // Added for completeness in Signup menu

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

// Note: Removed unused imports related to WindowAdapter/WindowEvent from Main.java as they are no longer needed here.

public class Main {
    public Main () {
        JFrame frame;
        JPanel topPanel, inPanel, bottomPanel;
        JLabel instituteLabel;
        //JLabel instituteLogoLabel;
        JButton signupButton, logiButton;
        JTextArea abouTextArea;

        Color DEEP_PERIWINKLE = new Color(0x735DA5);
        Color SOFT_LILAC = new Color(0xD3C5E5);
        Color SUBTLE_GRAY_BLUE = new Color(0xCEE6F2);

        frame = new JFrame("University Portal");
        frame.setSize(600,400);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        frame.setBackground(SUBTLE_GRAY_BLUE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(SUBTLE_GRAY_BLUE);
        //ImageIcon imageIcon = new ImageIcon("/home/sriram1o1/Sriram/Programs/OopsLab/LabProject/src/images/logo-black-1.png");
        //instituteLogoLabel = new JLabel(imageIcon);
        //topPanel.add(instituteLogoLabel, BorderLayout.WEST);
        instituteLabel = new JLabel("GATI SHAKTI VISHWAVIDYALAYA");
        topPanel.add(instituteLabel, BorderLayout.CENTER);
        inPanel = new JPanel();
        inPanel.setLayout(new BorderLayout());


        JPopupMenu loginMenu = new JPopupMenu();        
        JMenuItem studentLoginItem = new JMenuItem("Student Login");
        studentLoginItem.addActionListener(e -> {
            new Login(frame, "student");
            frame.setVisible(false);
        });
        loginMenu.add(studentLoginItem);
        JMenuItem professorLoginItem = new JMenuItem("Professor Login");
        professorLoginItem.addActionListener(e -> {
            new Login(frame, "prof");
            frame.setVisible(false);
        });
        loginMenu.add(professorLoginItem);
        JMenuItem adminLoginItem = new JMenuItem("Admin Login");
        adminLoginItem.addActionListener(e -> {
            new Login(frame, "admin");
            frame.setVisible(false);
        });
        loginMenu.add(adminLoginItem);
        
        JPopupMenu signupMenu = new JPopupMenu();
        JMenuItem studentSignupItem = new JMenuItem("Student Signup");
        studentSignupItem.addActionListener(e -> {
            StudentSignUp.studentSignUp(frame);
            frame.setVisible(false);
        });
        signupMenu.add(studentSignupItem);
        JMenuItem professorSignupItem = new JMenuItem("Professor Signup");
        professorSignupItem.addActionListener(e -> {
            ProfessorSignUp.professorSignUp(frame); 
            frame.setVisible(false);
        });
        signupMenu.add(professorSignupItem);
        // REMOVED: JMenuItem adminSignupItem and its action listener

        signupButton = new JButton("Signup");
        signupButton.setBackground(SOFT_LILAC);
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signupMenu.show(signupButton, 0, signupButton.getHeight());
            }
        });
        inPanel.add(signupButton, BorderLayout.EAST);

        logiButton = new JButton("Login");
        logiButton.setBackground(SOFT_LILAC);
        logiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginMenu.show(logiButton, 0, logiButton.getHeight());
            }
        });
        inPanel.add(logiButton, BorderLayout.WEST);

        topPanel.add(inPanel, BorderLayout.EAST);
        frame.add(topPanel, BorderLayout.NORTH);

        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        abouTextArea = new JTextArea("About:\nIn 2017, the Cabinet of India led by Prime Minister Narendra Modi approved \nthe setting up of India's first university exclusively focused \non transport-related education, multidisciplinary research and \ntraining, called National Rail and Transportation Institute (NRTI). \nIt was established in 2018.In 2022, NRTI was subsumed by Gati Shakti Vishwavidyalaya.\n\nMission:\n"
        + " Innovation-led, Industry-driven University for creating, assimilating and \nimparting excellence of knowledge and actions accelerating development \nin the transport and logistics sectors");
        abouTextArea.setEditable(false);
        //abouTextArea.setBackground(DEEP_PERIWINKLE);
        bottomPanel.add(abouTextArea, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.WEST);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);    
    }

    public static void main(String[] args) {
        Main mainwindow = new Main();
    
    }
}