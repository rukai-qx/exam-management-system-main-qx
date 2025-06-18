package com.exam;

import com.exam.ui.LoginFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class ExamManagementSystem 
{
    public static void main(String[] args) 
    {
        try 
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        } //setting the look and feel of the swing application

        SwingUtilities.invokeLater(() -> 
        {
            LoginFrame loginFrame = new LoginFrame(); /*Start the app by showing the Login screen first*/
            loginFrame.setVisible(true);
        });
    }
} 
