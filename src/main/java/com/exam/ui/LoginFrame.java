package com.exam.ui; //importing ui class..This file belongs in the folder structure: com/exam/ui.

import com.exam.constants.ScreenUtils;
import com.exam.model.Lecturer;
import com.exam.model.Student; // files that code needs is in this ui
import com.exam.model.User;
import com.exam.service.UserAuthentication;

import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;//Match an email,Check for numbers only,Validate a password format 

public class LoginFrame extends JFrame // If your class does NOT extend JFrame, you need to create a JFrame instance to
                                       // show a window.
{
    private JTextField emailField; // a text box on screen for email input
    private JPasswordField passwordField;// a password box on screen for hidden input
    private JComboBox<String> userTypeCombo;// a dropdown menu for selecting "Student" or "Lecturer"
    private UserAuthentication userService; // obj reference made of UserService class//service object that manages users
                                     // (not UI)

    public LoginFrame() // constructor  
    {
        userService = UserAuthentication.getUniqueInstance(); // Calling the getInstance() method that is defined inside the
                                                 // UserService class
        initializeUI(); // all attributes being initialized(buttons,labels,fields)//calling
    } /*
       * This means LoginFrame now has access to the users and login methods managed
       * by UserService.
       */

    private void initializeUI() 
    {
        setTitle("Exam Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // the red "X" on Windows
        setSize(500, 400);
        setLocationRelativeTo(null); // Center the window on the screen

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();//
        gbc.insets = new Insets(13, 13, 13, 13);

        Color goodBlue = new Color(28, 89, 161);// creating a color obj
        mainPanel.setBackground(goodBlue);

        // User Type Selection label 
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(createLabel("User Type:"), gbc); //Adding the label to mainPanel using the positioning and layout rules described in gbc
        //User Type combo textbox
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        userTypeCombo = new JComboBox<>(new String[] { "Student", "Lecturer" });
        userTypeCombo.setFont(new Font(null, Font.BOLD, 12));
        mainPanel.add(userTypeCombo, gbc);

        // Email Field label
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel emaiLabel = createLabel("Email:");
        mainPanel.add(emaiLabel, gbc);
        //email textbox
        gbc.gridx = 1;
        gbc.gridy = 1;
        emailField = new JTextField(30);
        mainPanel.add(emailField, gbc);

        // Password Field label
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(createLabel("Password:"), gbc);
        //password textbox
        gbc.gridx = 1;
        gbc.gridy = 2;
        passwordField = new JPasswordField(20);
        mainPanel.add(passwordField, gbc);

        // Login Button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // the login button should span across 2 columns
        gbc.fill = GridBagConstraints.CENTER;
        gbc.ipady = 10; //extra width of the login button
        gbc.ipadx = 10; //extra height "     "
        //button box obj 
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font(null, Font.BOLD, ScreenUtils.buttonTextSize));
        loginButton.addActionListener(e -> handleLogin());
        mainPanel.add(loginButton, gbc);
        add(mainPanel);
    }

    private JLabel createLabel(String name) //common createLabel method 
    {
        JLabel label = new JLabel(name);
        Font labelFont = new Font(null, Font.PLAIN, ScreenUtils.textFieldLabelTextSize);
        label.setFont(labelFont);
        label.setForeground(Color.white);
        return label;
    }

    private void handleLogin() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String userType = (String) userTypeCombo.getSelectedItem();

        if (!checkEmail(email)) {
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
                new LecturerHub((Lecturer) user).setVisible(true);
            } else {
                new StudentHub((Student) user).setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials");
        }
    }

    private boolean checkEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }
}
/*
 * Shows a window (because it extends JFrame)
 * 
 * Lets users select "Student" or "Lecturer"
 * 
 * Lets them enter email and password
 * 
 * On clicking login, checks the input and tries to log them in
 * 
 * If successful, shows their dashboard (next screen)
 */