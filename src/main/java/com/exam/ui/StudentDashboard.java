package com.exam.ui;

import com.exam.model.*;
import com.exam.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StudentDashboard extends JFrame {
    private Student student;
    private JList<Exam> availableExamsList;
    private DefaultListModel<Exam> availableExamsModel;
    private JList<ExamResult> resultsList;
    private DefaultListModel<ExamResult> resultsModel;
    private Timer examTimer;
    private int remainingTime;

    public StudentDashboard(Student student) {
        this.student = student;
        this.availableExamsModel = new DefaultListModel<>();
        this.resultsModel = new DefaultListModel<>();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Student Dashboard - " + student.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Top panel with welcome message
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome, " + student.getName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> handleLogout());
        topPanel.add(logoutButton, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center panel with split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        // Available exams panel
        JPanel availableExamsPanel = new JPanel(new BorderLayout());
        availableExamsPanel.setBorder(BorderFactory.createTitledBorder("Available Exams"));
        availableExamsList = new JList<>(availableExamsModel);
        availableExamsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane availableExamsScrollPane = new JScrollPane(availableExamsList);
        availableExamsPanel.add(availableExamsScrollPane, BorderLayout.CENTER);

        JButton takeExamButton = new JButton("Take Selected Exam");
        takeExamButton.addActionListener(e -> {
            Exam selectedExam = availableExamsList.getSelectedValue();
            if (selectedExam != null) {
                startExam(selectedExam);
            }
        });
        availableExamsPanel.add(takeExamButton, BorderLayout.SOUTH);

        // Results panel
        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setBorder(BorderFactory.createTitledBorder("Exam Results"));
        resultsList = new JList<>(resultsModel);
        resultsList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = resultsList.locationToIndex(e.getPoint());
                    if (index != -1) {
                        ExamResult selectedResult = resultsList.getModel().getElementAt(index);
                        Exam exam = selectedResult.getExam();
                        JOptionPane.showMessageDialog(StudentDashboard.this,
                            String.format(
                                "Exam: %s\nScore: %d\nCorrect: %d\nWrong: %d\nTotal Questions: %d",
                                exam.getTitle(),
                                selectedResult.getScore(),
                                selectedResult.getCorrectAnswers(),
                                selectedResult.getWrongAnswers(),
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

    private void refreshExamLists() {
        // Refresh available exams
        availableExamsModel.clear();
        List<Lecturer> lecturers = UserService.getInstance().getUsers().stream()
                .filter(user -> user instanceof Lecturer)
                .map(user -> (Lecturer) user)
                .toList();
        
        for (Lecturer lecturer : lecturers) {
            for (Exam exam : lecturer.getCreatedExams()) {
                boolean hasTaken = student.getExamResults().stream()
                        .anyMatch(result -> result.getExam().equals(exam));
                if (!hasTaken) {
                    availableExamsModel.addElement(exam);
                }
            }
        }

        // Refresh results
        resultsModel.clear();
        for (ExamResult result : student.getExamResults()) {
            resultsModel.addElement(result);
        }
    }

    private void startExam(Exam exam) {
        JDialog examDialog = new JDialog(this, "Taking Exam: " + exam.getTitle(), true);
        examDialog.setSize(800, 600);
        examDialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Timer panel
        JPanel timerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel timerLabel = new JLabel("Time Remaining: 30:00");
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
            JPanel questionPanel = new JPanel();
            questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
            questionPanel.setBorder(BorderFactory.createTitledBorder("Question " + (questionsPanel.getComponentCount() + 1)));

            JLabel questionLabel = new JLabel(question.getQuestionText());
            questionPanel.add(questionLabel);

            JPanel optionsPanel = new JPanel();
            optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
            ButtonGroup group = new ButtonGroup();
            JRadioButton[] options = new JRadioButton[question.getOptions().size()];

            for (int i = 0; i < question.getOptions().size(); i++) {
                JRadioButton option = new JRadioButton(question.getOptions().get(i));
                group.add(option);
                optionsPanel.add(option);
                options[i] = option;
            }
            answerGroups.add(options);
            questionPanel.add(optionsPanel);
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
                    if (options[j].isSelected() && j == question.getCorrectOptionIndex()) {
                        correctAnswers++;
                        break;
                    }
                }
            }

            ExamResult result = new ExamResult(student, exam);
            result.calculateScore(correctAnswers);
            student.addExamResult(result);
            refreshExamLists();
            examDialog.dispose();
            stopTimer();

            // Show result summary dialog
            int totalQuestions = exam.getQuestions().size();
            JOptionPane.showMessageDialog(this,
                String.format(
                    "Exam: %s\nScore: %d\nCorrect: %d\nWrong: %d\nTotal Questions: %d",
                    exam.getTitle(),
                    result.getScore(),
                    result.getCorrectAnswers(),
                    result.getWrongAnswers(),
                    totalQuestions
                ),
                "Exam Result",
                JOptionPane.INFORMATION_MESSAGE
            );
        });
        mainPanel.add(submitButton, BorderLayout.SOUTH);

        examDialog.add(mainPanel);

        // Start timer
        remainingTime = exam.getTimeLimitMinutes() * 60;
        startTimer(timerLabel, examDialog, submitButton);

        examDialog.setVisible(true);
    }

    private void startTimer(JLabel timerLabel, JDialog examDialog, JButton submitButton) {
        examTimer = new Timer();
        examTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                remainingTime--;
                if (remainingTime <= 0) {
                    SwingUtilities.invokeLater(() -> {
                        submitButton.doClick();
                    });
                } else {
                    SwingUtilities.invokeLater(() -> {
                        int minutes = remainingTime / 60;
                        int seconds = remainingTime % 60;
                        timerLabel.setText(String.format("Time Remaining: %02d:%02d", minutes, seconds));
                    });
                }
            }
        }, 0, 1000);
    }

    private void stopTimer() {
        if (examTimer != null) {
            examTimer.cancel();
            examTimer = null;
        }
    }

    private void handleLogout() {
        stopTimer();
        this.dispose();
        new LoginFrame().setVisible(true);
    }
} 