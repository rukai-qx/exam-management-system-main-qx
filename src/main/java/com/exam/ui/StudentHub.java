package com.exam.ui;

import com.exam.model.*;
import com.exam.service.UserAuthentication;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StudentHub extends JFrame //main connection,making StudentHub a window application
{
    private Student stud; //Student obj holds info about logged-in stud
    private JList<Exam> availableExamsList; //List of available exams
    private DefaultListModel<Exam> availableExamsModel; //holds Exam obj which encapsulate all details of it(attributes&methods)
    private JList<ExamResult> resultsList;
    private DefaultListModel<ExamResult> resultsModel; //Model provides data for JList & directly connected
    private Timer examTimer;
    private int remainingTime; //to store the remaining time

    public StudentHub(Student stud) //called when a new obj of StudentHub is created
    {
        this.stud = stud;
        this.availableExamsModel = new DefaultListModel<>();
        this.resultsModel = new DefaultListModel<>();
        initializeUI(); //this method encapsulates the UI setup & sets up StudentHub window
    }

    private void initializeUI() //sets up elements of StudentHub
    {
        setTitle("Student Hub - " + stud.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //tells the application exit when user closes the window
        setSize(800, 600);
        setLocationRelativeTo(null); //screen to appear in center

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout()); //main container for all UI components

        // welcome panel with welcome message
        JPanel welcomePanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome, " + stud.getName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomePanel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> handleLogout());
        welcomePanel.add(logoutButton, BorderLayout.EAST); 

        mainPanel.add(welcomePanel, BorderLayout.NORTH);

        // Center panel with split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT); //a swing container which divides its area to 2 containers
        
        // Available exams panel
        JPanel availableExamsPanel = new JPanel(new BorderLayout());   //left component in splitPane
        availableExamsPanel.setBorder(BorderFactory.createTitledBorder("Available Exams"));
        availableExamsList = new JList<>(availableExamsModel); //initializing the Jlist//where list gets Exam objs from model
        availableExamsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //sets select only one Exam obj at a time
        JScrollPane availableExamsScrollPane = new JScrollPane(availableExamsList);
        availableExamsPanel.add(availableExamsScrollPane, BorderLayout.CENTER);

        JButton doExamButton = new JButton("Do the Selected Exam");
        doExamButton.addActionListener(e -> //e->mouseEvent obj
        {
            Exam selectedExam = availableExamsList.getSelectedValue();
            if (selectedExam != null) 
            {
                startExam(selectedExam); //calling the method
            }
        });
        availableExamsPanel.add(doExamButton, BorderLayout.SOUTH);

        // Results panel
        JPanel resultsPanel = new JPanel(new BorderLayout()); //right component in splitPane
        resultsPanel.setBorder(BorderFactory.createTitledBorder("Exam Results"));
        resultsList = new JList<>(resultsModel); //initializing the JList
        resultsList.addMouseListener(new java.awt.event.MouseAdapter() //addMouseListener to detect mouse events
        {
            @Override //overriding MouseAdapter cls' method which had no implementation
            public void mouseClicked(java.awt.event.MouseEvent e) 
            {
                if (e.getClickCount() == 2) 
                {
                    int index = resultsList.locationToIndex(e.getPoint());
                    if (index != -1) 
                    {
                        ExamResult selectedResult = resultsList.getModel().getElementAt(index);
                        Exam exam = selectedResult.getExam();
                        JOptionPane.showMessageDialog(StudentHub.this,String.format //summary of their exam performance
                            
                            (
                                "Exam: %s\nScore: %d\nCorrect: %d\nWrong: %d\nTotal Questions: %d",
                                exam.getTitle(),
                                selectedResult.getMarks(),
                                selectedResult.getCorrectAnswerTot(),
                                selectedResult.getWrongAnswerTot(),
                                exam.getQuestions().size()
                            ),
                            "Exam Result",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                }
            }
        });
        JScrollPane resultsScrollPane = new JScrollPane(resultsList);
        resultsPanel.add(resultsScrollPane, BorderLayout.CENTER);

        splitPane.setLeftComponent(availableExamsPanel);
        splitPane.setRightComponent(resultsPanel);
        splitPane.setDividerLocation(400);

        mainPanel.add(splitPane, BorderLayout.CENTER);

        add(mainPanel);
        refreshExamLists();
    }

    private void refreshExamLists() //after one exam is finished, the list needs to be refreshed to show the newly completed exam
    {
        // Refresh available exams
        availableExamsModel.clear();
        List<Lecturer> lecturers = UserAuthentication.getUniqueInstance().getUsers().stream()
                .filter(user -> user instanceof Lecturer)
                .map(user -> (Lecturer) user)
                .toList();
        
        for (Lecturer lecturer : lecturers) {
            for (Exam exam : lecturer.getCreatedExams()) {
                boolean hasTaken = stud.getResultsInfo().stream()
                        .anyMatch(result -> result.getExam().equals(exam));
                if (!hasTaken) {
                    availableExamsModel.addElement(exam);
                }
            }
        }

        // Refresh the results
        resultsModel.clear();
        for (ExamResult result : stud.getResultsInfo()) 
        {
            resultsModel.addElement(result);
        }
    }

    private void startExam(Exam exam) 
    {
        JDialog examDialog = new JDialog(this, "Taking Exam: " + exam.getTitle(), true);
        examDialog.setSize(800, 600);
        examDialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Timer panel
        JPanel timerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel timerLabel = new JLabel("Time Remaining: 30:00");
        Color lightYellow  = new Color(255, 255, 153);// creating a color obj
        timerPanel.setBackground(lightYellow);
        timerPanel.add(timerLabel);
        mainPanel.add(timerPanel, BorderLayout.NORTH);

        // Questions panel
        JPanel questionsPanel = new JPanel();
        questionsPanel.setLayout(new BoxLayout(questionsPanel, BoxLayout.Y_AXIS));

        JScrollPane questionsScrollPane = new JScrollPane(questionsPanel);
        mainPanel.add(questionsScrollPane, BorderLayout.CENTER);

        // Create question panels
        List<JRadioButton[]> answerGroups = new ArrayList<>();
        for (Question question : exam.getQuestions()) {
            JPanel questionPanel = new JPanel(); //panel for each individual question
            questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
            questionPanel.setBorder(BorderFactory.createTitledBorder("Question " + (questionsPanel.getComponentCount() + 1)));

            JLabel questionLabel = new JLabel(question.getGivenQuestion());
            questionPanel.add(questionLabel);

            JPanel answersPanel = new JPanel();
            answersPanel.setLayout(new BoxLayout(answersPanel, BoxLayout.Y_AXIS));

            ButtonGroup group = new ButtonGroup();
            JRadioButton[] options = new JRadioButton[question.getOptions().size()];

            for (int i = 0; i < question.getOptions().size(); i++) 
            {
                JRadioButton option = new JRadioButton(question.getOptions().get(i));
                group.add(option);
                answersPanel.add(option);
                options[i] = option;
            }
            answerGroups.add(options);
            questionPanel.add(answersPanel);
            questionsPanel.add(questionPanel);
        }

        // Submit button
        JButton submitButton = new JButton("Submit Exam");
        submitButton.addActionListener(e -> {
            int correctAnswers = 0;
            for (int i = 0; i < exam.getQuestions().size(); i++) {
                Question question = exam.getQuestions().get(i);
                JRadioButton[] options = answerGroups.get(i);
                for (int j = 0; j < options.length; j++) {
                    if (options[j].isSelected() && j == question.getCorrectAnswerIndex()) {
                        correctAnswers++;
                        break;
                    }
                }
            }

            ExamResult result = new ExamResult(stud, exam);
            result.calculateMarks(correctAnswers);
            stud.addExamResult(result);
            refreshExamLists();
            examDialog.dispose();
            stopTimer();

            //Show result summary dialog box
            int totalQuestions = exam.getQuestions().size();
            JOptionPane.showMessageDialog(this,
                String.format(
                    "Exam: %s\nScore: %d\nCorrect: %d\nWrong: %d\nTotal Questions: %d",
                    exam.getTitle(),
                    result.getMarks(),
                    result.getCorrectAnswerTot(),
                    result.getWrongAnswerTot(),
                    totalQuestions
                ),
                "Exam Result",
                JOptionPane.INFORMATION_MESSAGE
            );
        });
        mainPanel.add(submitButton, BorderLayout.SOUTH);

        examDialog.add(mainPanel);

        // Start timer
        remainingTime = exam.getTimeLimitMins() * 60;
        startTimer(timerLabel, examDialog, submitButton);

        examDialog.setVisible(true);
    }

    private void startTimer(JLabel timerLabel, JDialog examDialog, JButton submitButton) 
    {
        examTimer = new Timer(); //creates obj of Timer class
        examTimer.scheduleAtFixedRate(new TimerTask() //TimerTask class is an abstract class
        {
            @Override
            public void run() //an abstract method inherited from TimerTask class where we give the implementation
            {                   //run() belongs to timerTask class that examTimer(instance variable) is asked to execute it.
                remainingTime--; //decrements remainingtime variable by 1
                if (remainingTime <= 0) 
                {
                    SwingUtilities.invokeLater(() -> 
                    {
                        submitButton.doClick();//if the time has expired,it automatically clicks submit button 
                    });
                } 
                else 
                {
                    SwingUtilities.invokeLater(() -> 
                    {
                        int minutes = remainingTime / 60;
                        int seconds = remainingTime % 60;
                        timerLabel.setText(String.format("Time Remaining: %02d:%02d", minutes, seconds));
                    });
                }
            }
        }, 0, 1000);
    }

    private void stopTimer() 
    {
        if (examTimer != null) 
        {
            examTimer.cancel();
            examTimer = null;
        }
    }

    private void handleLogout() 
    {
        stopTimer();
        this.dispose();
        new LoginFrame().setVisible(true);
    }
} 