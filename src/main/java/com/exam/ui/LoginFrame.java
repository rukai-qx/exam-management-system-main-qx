package com.exam.ui;

import com.exam.model.Lecturer;
import com.exam.model.Student;
import com.exam.model.User;
import com.exam.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeCombo;
    private UserService userService;

    public LoginFrame() {
        userService = UserService.getInstance();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Exam Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // User Type Selection
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("User Type:"), gbc);

        gbc.gridx = 1;
        userTypeCombo = new JComboBox<>(new String[]{"Student", "Lecturer"});
        mainPanel.add(userTypeCombo, gbc);

        // Email Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        emailField = new JTextField(20);
        mainPanel.add(emailField, gbc);

        // Password Field
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        mainPanel.add(passwordField, gbc);

        // Login Button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> handleLogin());
        mainPanel.add(loginButton, gbc);

        add(mainPanel);
    }

    private void handleLogin() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String userType = (String) userTypeCombo.getSelectedItem();

        if (!validateEmail(email)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address");
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters long");
            return;
        }

        User user = userService.login(email, password, userType);
        if (user != null) {
            this.dispose();
            if (user instanceof Lecturer) {
                new LecturerDashboard((Lecturer) user).setVisible(true);
            } else {
                new StudentDashboard((Student) user).setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials");
        }
    }

    private boolean validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }
} 