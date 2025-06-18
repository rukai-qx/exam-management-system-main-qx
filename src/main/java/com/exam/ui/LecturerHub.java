package com.exam.ui;

import com.exam.model.Exam;
import com.exam.model.Lecturer;
import com.exam.model.Question; //classes related to this ui interface

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.ArrayList;
import java.util.List;


public class LecturerHub extends JFrame 
{
    private Lecturer assignedLecturer;
    private JList<Exam> prepExamList; // list of Exam objects(types) //ref variable is examList
    private DefaultListModel<Exam> examListModel;//variable is accessible only within this class//encapsulation

    public LecturerHub(Lecturer assignedLecturer) 
    {
        this.assignedLecturer = assignedLecturer;
        this.examListModel = new DefaultListModel<>();
        initializeUI();
    }

    private void initializeUI() 
    {
        setTitle("Lecturer Hub - " + assignedLecturer.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        

        // Top panel with welcome message and create exam button
        JPanel welcomePanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome, " + assignedLecturer.getName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomePanel.add(welcomeLabel, BorderLayout.WEST); //top panel
        
        JButton createExamButton = new JButton("Create New Exam");
        createExamButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e)
            {
                showCreateExamDialog(); //method we created
            }
            
        });
        welcomePanel.add(createExamButton,BorderLayout.EAST);
        mainPanel.add(welcomePanel, BorderLayout.NORTH);//Adds the top panel to the top of the main panel.

        // Center panel with exam list
        prepExamList = new JList<>(examListModel); //ref variable of Model
        prepExamList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//Restricts users to selecting only one exam at a time (no multiple selections)
        Color lavenderPurple  = new Color(230,230,250);// creating a color obj
        prepExamList.setBackground(lavenderPurple);

        prepExamList.addMouseListener(new MouseAdapter() 
        {
            public void mouseClicked(MouseEvent e)
            {
                if(e.getClickCount()==2)
                {
                    int index = prepExamList.locationToIndex(e.getPoint());
                    if(index != -1)
                    {
                        Exam selectedExam = prepExamList.getModel().getElementAt(index);
                        showEnrolledStudentsDialog(selectedExam);
                    }
                }
            }
        });
       
        JScrollPane scrollPane = new JScrollPane(prepExamList);/*Wraps the exam list in a scroll pane
                                                            Allows scrolling when there are many exams (more than can fit on screen)*/
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom panel with logout button
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutButton = new JButton("Logout");
    
        logoutButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e)
            {
                handleLogout();
            }
        });
        logoutPanel.add(logoutButton);
        mainPanel.add(logoutPanel, BorderLayout.SOUTH);

        add(mainPanel);
        refreshExamList();
    }
    //creating the dialog window
    private void showCreateExamDialog() //this method works when createNewExam btn is pressed
    {
        JDialog dialogTop = new JDialog(this, "Create New Exam", true);//modal dialog's title bar
        dialogTop.setSize(500, 500);
        dialogTop.setLocationRelativeTo(this); //this means Lecturedashboard window(current obj/class)

        JPanel firstPanel = new JPanel(new BorderLayout());
        
        JPanel titlePanel = new JPanel(new BorderLayout());//panel for exam title
        JLabel titleLabel = new JLabel("Exam Title is:");
        titlePanel.add(titleLabel,BorderLayout.WEST); //adding titleLabel on titlePanel

        JTextField titleField = new JTextField(40); 
        titlePanel.add(titleField); //adding the textfield to the title panel
        firstPanel.add(titlePanel, BorderLayout.NORTH);

        // Questions panel
        JPanel allQuestionsPanel = new JPanel();//Creates a panel that will hold all the individual questions
        allQuestionsPanel.setLayout(new BoxLayout(allQuestionsPanel, BoxLayout.Y_AXIS)); //Each question will appear one below the other
        allQuestionsPanel.setAlignmentY(Component.TOP_ALIGNMENT);//Aligns all questions to the top of the panel
        
        Color cadetBlueColor  = new Color(240, 248, 255);// creating a color obj
        allQuestionsPanel.setBackground(cadetBlueColor);

        JScrollPane questionsScroll = new JScrollPane(allQuestionsPanel);
        firstPanel.add(questionsScroll, BorderLayout.CENTER); //:::::

        List<Question> questions = new ArrayList<>();
        
        JButton addQuestionButton = new JButton("Add Question");
        addQuestionButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                Question question = new Question("");
                questions.add(question);
                JPanel makeQuestionPanel = createQuestionPanel(question, questions); //Calls your custom method createQuestionPanel() to build the UI,all ques in a scrollpane
                
                allQuestionsPanel.add(makeQuestionPanel);
                allQuestionsPanel.revalidate();
                allQuestionsPanel.repaint(); 
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addQuestionButton);
        JButton saveButton = new JButton("Save Exam");
        saveButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e)
            {
                String title = titleField.getText();
                if (title.isEmpty()) 
                {
                    JOptionPane.showMessageDialog(dialogTop, "Enter an exam title");
                    return;
                }
                if (questions.isEmpty()) 
                {
                    JOptionPane.showMessageDialog(dialogTop, "Add at least one question");
                    return;
                }

                // Validate that all questions have text, options, and a correct answer
                for (Question question : questions) 
                {
                    if (question.getGivenQuestion().isEmpty()) 
                    {
                        JOptionPane.showMessageDialog(dialogTop, "All questions must have text");
                        return;// STOPS here - GOOD!, Code below never runs
                    }
                    if (question.getOptions().isEmpty()) 
                    {
                        JOptionPane.showMessageDialog(dialogTop, "All questions should give one option");
                        return;
                    }
                    if (question.getOptions().size() < 2) 
                    {
                        JOptionPane.showMessageDialog(dialogTop, "All questions must have at least two options");
                        return;
                    }
                    if (question.getCorrectAnswerIndex() < 0) 
                    {
                        JOptionPane.showMessageDialog(dialogTop, "All questions must have a correct answer selected");
                        return;
                    }
                }

                Exam exam = new Exam(title, assignedLecturer); //class exam
                for (Question question : questions) //questions saved in arrayList
                {
                    exam.addQuestion(question);
                }
                assignedLecturer.createExam(exam);
                refreshExamList();
                dialogTop.dispose();
            }
            
        });
        buttonPanel.add(saveButton);
        firstPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialogTop.add(firstPanel);
        dialogTop.setVisible(true);
    }

    private JPanel createQuestionPanel(Question question, List<Question> questions) 
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Question"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JPanel questionHeaderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JTextField questionField = new JTextField(30);
        Dimension smallTextFieldDim = new Dimension(300, 24);
        questionField.setPreferredSize(smallTextFieldDim);
        questionField.setMaximumSize(new Dimension(300, 60));
        JButton deleteQuestionButton = new JButton("Delete");
        // Add any button styling or listeners you need
        deleteQuestionButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                
                int confirm = JOptionPane.showConfirmDialog(
                panel,
                "Are you sure you want to delete this question?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) 
                {
                    Container parent = panel.getParent();
                    if (parent != null) 
                    {
                        parent.remove(panel);
                        parent.revalidate();
                        parent.repaint();
                        questions.remove(question);
                    }
                }
            }
        });
        //holds the value in textfield
        questionField.getDocument().addDocumentListener(new DocumentListener() 
        {
            @Override
            public void changedUpdate(DocumentEvent e) 
            {
                handleTextChange();
            }
    
            @Override
            public void removeUpdate(DocumentEvent e) 
            {
                handleTextChange();
            }
    
            @Override
            public void insertUpdate(DocumentEvent e) 
            {
                handleTextChange();
            }
    
            private void handleTextChange() 
            {
                // Resize logic
                int lines = questionField.getText().split("\n").length;
                int height = Math.max(24, Math.min(24 * lines, 60));
                questionField.setPreferredSize(new Dimension(300, height));
                questionField.revalidate();
        
                // Update logic
                question.setGivenQuestion(questionField.getText());
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
        //Add Option button
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
       

        optionField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() 
        {
            public void changedUpdate(javax.swing.event.DocumentEvent e) 
            { 
                update(); 
            }
            public void removeUpdate(javax.swing.event.DocumentEvent e) 
            { 
                update(); 
            }
            public void insertUpdate(javax.swing.event.DocumentEvent e) 
            { 
                update(); 
            }
            private void update() 
            {
                question.getOptions().clear();
                for (JTextField field : optionFields) 
                {
                    question.addOption(field.getText());
                }
            }
        });

        JButton deleteOptionButton = new JButton("Delete");
        deleteOptionButton.setPreferredSize(new Dimension(80, 24));
        deleteOptionButton.setMaximumSize(new Dimension(80, 24));
        deleteOptionButton.addActionListener(e2 -> 
        {
                int confirm = JOptionPane.showConfirmDialog
                (
                    singleOptionPanel,
                    "Are you sure you want to delete this option?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) 
                {
                    optionFields.remove(optionField);
                    optionButtons.remove(radioButton);
                    group.remove(radioButton);
                    optionsPanel.remove(singleOptionPanel);
                    optionsPanel.revalidate();
                    optionsPanel.repaint();
                    question.getOptions().clear();
                    for (JTextField field : optionFields) 
                    {
                        question.addOption(field.getText());
                    }
                }
        });

        radioButton.addActionListener(e2 -> 
        {
            int index = optionButtons.indexOf(radioButton);
            question.setCorrectAnswer(index);
        });
        singleOptionPanel.add(radioButton);
        singleOptionPanel.add(optionField);
        singleOptionPanel.add(deleteOptionButton);
        singleOptionPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        optionsPanel.add(singleOptionPanel);
        //  Remove any existing glue before adding a new one
        for (int i = optionsPanel.getComponentCount() - 1; i >= 0; i--) 
        {
            if (optionsPanel.getComponent(i) instanceof Box.Filler) 
            {
                optionsPanel.remove(i);
            }
        }
         
        
        optionsPanel.add(Box.createVerticalGlue());
            
        optionsPanel.revalidate();
            
        optionsPanel.repaint();
        });

        panel.add(optionsPanel);
        panel.add(addOptionButton);

        return panel;
    }

    private void refreshExamList() {
        examListModel.clear();
        for (Exam exam : assignedLecturer.getCreatedExams()) {
            examListModel.addElement(exam);
        }
    }

    private void handleLogout() {
        this.dispose();
        new LoginFrame().setVisible(true);
    }

    private void showEnrolledStudentsDialog(Exam exam) {
        java.util.List<com.exam.model.User> users = com.exam.service.UserAuthentication.getUniqueInstance().getUsers();
        java.util.List<com.exam.model.Student> students = new java.util.ArrayList<>();
        for (com.exam.model.User user : users) {
            if (user instanceof com.exam.model.Student) {
                students.add((com.exam.model.Student) user);
            }
        }
        java.util.List<com.exam.model.ExamResult> results = new java.util.ArrayList<>();
        for (com.exam.model.Student student : students) {
            for (com.exam.model.ExamResult result : student.getResultsInfo()) {
                if (result.getExam().equals(exam)) {
                    results.add(result);
                }
            }
        }
        String[] columnNames = {"Name", "Email", "Marks"};
        Object[][] data = new Object[results.size()][3];
        for (int i = 0; i < results.size(); i++) {
            com.exam.model.ExamResult result = results.get(i);
            data[i][0] = result.getStudent().getName();
            data[i][1] = result.getStudent().getEmail();
            data[i][2] = result.getMarks();
        }
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        JOptionPane.showMessageDialog(this, scrollPane, "Enrolled Students for '" + exam.getTitle() + "'", JOptionPane.INFORMATION_MESSAGE);
    }
} 