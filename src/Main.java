import javax.swing.*;

import com.gsv.utils.UserTypeSelection;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

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

        signupButton = new JButton("Signup");
        signupButton.setBackground(SOFT_LILAC);
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserTypeSelection usertypeSelec = new UserTypeSelection(frame,"signup");
                frame.setVisible(false);
            }
        });
        inPanel.add(signupButton, BorderLayout.EAST);

        logiButton = new JButton("Login");
        logiButton.setBackground(SOFT_LILAC);
        logiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserTypeSelection usertypeSelec = new UserTypeSelection(frame,"login");
                frame.setVisible(false);
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
