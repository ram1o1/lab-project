import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;


public class Login {
    public Login(JFrame previousFrame) {
        JFrame frame;
        JPanel logiPanel, inputPanel, buttonPanel;
        JLabel usernamLabel, passwordlLabel;
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
        inputPanel.setLayout(new GridLayout(2,2));
        usernamLabel = new JLabel("Username ");
        inputPanel.add(usernamLabel);
        usernameField = new JTextField();
        inputPanel.add(usernameField);
        passwordlLabel = new JLabel("Password ");
        inputPanel.add(passwordlLabel);
        passwordField = new JTextField();
        inputPanel.add(passwordField);
        logiPanel.add(inputPanel, BorderLayout.CENTER);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,2));

        backButton = new JButton("back ");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main mainWindow = new Main();
                frame.dispose();
            }
        });
        buttonPanel.add(backButton);

        logiButton = new JButton("Login ");
        buttonPanel.add(logiButton);

        logiPanel.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(logiPanel);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                previousFrame.setVisible(true);
            }
        });

        frame.setVisible(true);

    }
}
