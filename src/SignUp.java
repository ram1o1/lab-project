import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;


public class SignUp {
    public SignUp(JFrame previousFrame, String userType) {
        JFrame frame;
        JPanel signUpPanel, inputPanel, buttonPanel;
        JLabel usernamLabel, passwordlLabel;
        JTextField usernameField, passwordField;
        JButton backButton, signUpButton;

        frame = new JFrame();
        frame.setSize(600,400);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        frame.setLayout(new BorderLayout());

        signUpPanel = new JPanel();
        signUpPanel.setLayout(new BorderLayout());

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
        signUpPanel.add(inputPanel, BorderLayout.CENTER);

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

        signUpButton = new JButton("signup ");
        buttonPanel.add(signUpButton);

        signUpPanel.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(signUpPanel);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                previousFrame.setVisible(true);
            }
        });

        frame.setVisible(true);

    }
}
