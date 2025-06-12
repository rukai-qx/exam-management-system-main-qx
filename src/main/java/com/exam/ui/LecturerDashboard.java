package com.exam.ui;

import com.exam.model.Exam;
import com.exam.model.Lecturer;
import com.exam.model.Question;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LecturerDashboard extends JFrame {
    private Lecturer lecturer;
    private JList<Exam> examList;
    private DefaultListModel<Exam> examListModel;

    public LecturerDashboard(Lecturer lecturer) {
        this.lecturer = lecturer;
        this.examListModel = new DefaultListModel<>();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Lecturer Dashboard - " + lecturer.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Top panel with welcome message and create exam button
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome, " + lecturer.getName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton createExamButton = new JButton("Create New Exam");
        createExamButton.addActionListener(e -> showCreateExamDialog());
        topPanel.add(createExamButton, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center panel with exam list
        examList = new JList<>(examListModel);
        examList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        examList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = examList.locationToIndex(e.getPoint());
                    if (index != -1) {
                        Exam selectedExam = examList.getModel().getElementAt(index);
                        showEnrolledStudentsDialog(selectedExam);
                    }
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(examList);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom panel with logout button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> handleLogout());
        bottomPanel.add(logoutButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        refreshExamList();
    }

    private void showCreateExamDialog() {
        JDialog dialog = new JDialog(this, "Create New Exam", true);
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout());
        
        // Title input
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.add(new JLabel("Exam Title:"));
        JTextField titleField = new JTextField(40);
        titlePanel.add(titleField);
        panel.add(titlePanel, BorderLayout.NORTH);

        // Questions panel
        JPanel questionsPanel = new JPanel();
        questionsPanel.setLayout(new BoxLayout(questionsPanel, BoxLayout.Y_AXIS));
        questionsPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        JScrollPane questionsScrollPane = new JScrollPane(questionsPanel);
        panel.add(questionsScrollPane, BorderLayout.CENTER);

        List<Question> questions = new ArrayList<>();
        JButton addQuestionButton = new JButton("Add Question");
        addQuestionButton.addActionListener(e -> {
            Question question = new Question("");
            questions.add(question);
            JPanel questionPanel = createQuestionPanel(question, questions);
            questionsPanel.add(questionPanel);
            int count = questionsPanel.getComponentCount();
            if (count > 1 && questionsPanel.getComponent(count - 2) instanceof Box.Filler) {
                questionsPanel.remove(count - 2);
            }
            questionsPanel.add(Box.createVerticalGlue());
            questionsPanel.revalidate();
            questionsPanel.repaint();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addQuestionButton);
        JButton saveButton = new JButton("Save Exam");
        saveButton.addActionListener(e -> {
            String title = titleField.getText();
            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter an exam title");
                return;
            }
            if (questions.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please add at least one question");
                return;
            }

            // Validate that all questions have text, options, and a correct answer
            for (Question question : questions) {
                if (question.getQuestionText().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "All questions must have text");
                    return;
                }
                if (question.getOptions().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "All questions must have at least one option");
                    return;
                }
                if (question.getOptions().size() < 2) {
                    JOptionPane.showMessageDialog(dialog, "All questions must have at least two options");
                    return;
                }
                if (question.getCorrectOptionIndex() < 0) {
                    JOptionPane.showMessageDialog(dialog, "All questions must have a correct answer selected");
                    return;
                }
            }

            Exam exam = new Exam(title, lecturer);
            for (Question question : questions) {
                exam.addQuestion(question);
            }
            lecturer.createExam(exam);
            refreshExamList();
            dialog.dispose();
        });
        buttonPanel.add(saveButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private JPanel createQuestionPanel(Question question, List<Question> questions) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Question"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Question header panel with delete button
        JPanel questionHeaderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JTextField questionField = new JTextField(30);
        Dimension smallTextFieldDim = new Dimension(300, 24);
        questionField.setPreferredSize(smallTextFieldDim);
        questionField.setMaximumSize(new Dimension(300, 60));
        questionField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { resize(); update(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { resize(); update(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { resize(); update(); }
            private void update() {
                question.setQuestionText(questionField.getText());
            }
            private void resize() {
                int lines = questionField.getText().split("\n").length;
                int height = Math.max(24, Math.min(24 * lines, 60));
                questionField.setPreferredSize(new Dimension(300, height));
                questionField.revalidate();
            }
        });
        JButton deleteQuestionButton = new JButton("Delete Question");
        deleteQuestionButton.setPreferredSize(new Dimension(120, 24));
        deleteQuestionButton.setMaximumSize(new Dimension(120, 24));
        deleteQuestionButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                panel,
                "Are you sure you want to delete this question?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                Container parent = panel.getParent();
                if (parent != null) {
                    parent.remove(panel);
                    parent.revalidate();
                    parent.repaint();
                    questions.remove(question);
                }
            }
        });
        questionHeaderPanel.add(questionField);
        questionHeaderPanel.add(deleteQuestionButton);
        panel.add(questionHeaderPanel);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        List<JTextField> optionFields = new ArrayList<>();
        List<JRadioButton> optionButtons = new ArrayList<>();
        ButtonGroup group = new ButtonGroup();

        JButton addOptionButton = new JButton("Add Option");
        addOptionButton.setPreferredSize(new Dimension(120, 24));
        addOptionButton.setMaximumSize(new Dimension(120, 24));
        addOptionButton.addActionListener(e -> {
            JPanel singleOptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            singleOptionPanel.setPreferredSize(new Dimension(0, 24));
            singleOptionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
            JRadioButton radioButton = new JRadioButton();
            group.add(radioButton);
            optionButtons.add(radioButton);

            JTextField optionField = new JTextField(20);
            optionField.setPreferredSize(new Dimension(220, 24));
            optionField.setMaximumSize(new Dimension(220, 60));
            optionFields.add(optionField);
            optionField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                public void changedUpdate(javax.swing.event.DocumentEvent e) { update(); }
                public void removeUpdate(javax.swing.event.DocumentEvent e) { update(); }
                public void insertUpdate(javax.swing.event.DocumentEvent e) { update(); }
                private void update() {
                    question.getOptions().clear();
                    for (JTextField field : optionFields) {
                        question.addOption(field.getText());
                    }
                }
            });

            JButton deleteOptionButton = new JButton("Delete");
            deleteOptionButton.setPreferredSize(new Dimension(80, 24));
            deleteOptionButton.setMaximumSize(new Dimension(80, 24));
            deleteOptionButton.addActionListener(e2 -> {
                int confirm = JOptionPane.showConfirmDialog(
                    singleOptionPanel,
                    "Are you sure you want to delete this option?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    optionFields.remove(optionField);
                    optionButtons.remove(radioButton);
                    group.remove(radioButton);
                    optionsPanel.remove(singleOptionPanel);
                    optionsPanel.revalidate();
                    optionsPanel.repaint();
                    question.getOptions().clear();
                    for (JTextField field : optionFields) {
                        question.addOption(field.getText());
                    }
                }
            });

            radioButton.addActionListener(e2 -> {
                int index = optionButtons.indexOf(radioButton);
                question.setCorrectOption(index);
            });

            singleOptionPanel.add(radioButton);
            singleOptionPanel.add(optionField);
            singleOptionPanel.add(deleteOptionButton);
            singleOptionPanel.setAlignmentY(Component.TOP_ALIGNMENT);
            optionsPanel.add(singleOptionPanel);
            // Remove any existing glue before adding a new one
            for (int i = optionsPanel.getComponentCount() - 1; i >= 0; i--) {
                if (optionsPanel.getComponent(i) instanceof Box.Filler) {
                    optionsPanel.remove(i);
                }
            }
            optionsPanel.add(Box.createVerticalGlue());
            optionsPanel.revalidate();
            optionsPanel.repaint();
        });

        panel.add(optionsPanel);
        panel.add(Box.createVerticalStrut(8)); // vertical spacing
        panel.add(addOptionButton);

        return panel;
    }

    private void refreshExamList() {
        examListModel.clear();
        for (Exam exam : lecturer.getCreatedExams()) {
            examListModel.addElement(exam);
        }
    }

    private void handleLogout() {
        this.dispose();
        new LoginFrame().setVisible(true);
    }

    private void showEnrolledStudentsDialog(Exam exam) {
        java.util.List<com.exam.model.User> users = com.exam.service.UserService.getInstance().getUsers();
        java.util.List<com.exam.model.Student> students = new java.util.ArrayList<>();
        for (com.exam.model.User user : users) {
            if (user instanceof com.exam.model.Student) {
                students.add((com.exam.model.Student) user);
            }
        }
        java.util.List<com.exam.model.ExamResult> results = new java.util.ArrayList<>();
        for (com.exam.model.Student student : students) {
            for (com.exam.model.ExamResult result : student.getExamResults()) {
                if (result.getExam().equals(exam)) {
                    results.add(result);
                }
            }
        }
        String[] columnNames = {"Name", "Email", "Score"};
        Object[][] data = new Object[results.size()][3];
        for (int i = 0; i < results.size(); i++) {
            com.exam.model.ExamResult result = results.get(i);
            data[i][0] = result.getStudent().getName();
            data[i][1] = result.getStudent().getEmail();
            data[i][2] = result.getScore();
        }
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        JOptionPane.showMessageDialog(this, scrollPane, "Enrolled Students for '" + exam.getTitle() + "'", JOptionPane.INFORMATION_MESSAGE);
    }
} 