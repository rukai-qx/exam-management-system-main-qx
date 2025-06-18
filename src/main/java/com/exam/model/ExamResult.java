package com.exam.model;

import java.util.Date;

public class ExamResult 
{
    private Student student;
    private Exam exam;
    private int marks;
    private int correctAnswerTot;
    private int wrongAnswerTot;
    private Date submissionTime;
    private boolean isEvaluated;

    public ExamResult(Student student, Exam exam) 
    {
        this.student = student;
        this.exam = exam;
        this.marks = 0;
        this.correctAnswerTot = 0;
        this.wrongAnswerTot = 0;
        this.submissionTime = new Date();
        this.isEvaluated = false;
    }

    public void calculateMarks(int correctAnswerTot) 
    {
        this.correctAnswerTot = correctAnswerTot;
        this.wrongAnswerTot = exam.getQuestions().size() - correctAnswerTot;
        this.marks = (correctAnswerTot * 100) / exam.getQuestions().size();
        this.isEvaluated = true;
    }

    public Student getStudent() 
    {
        return student;
    }

    public Exam getExam() 
    {
        return exam;
    }

    public int getMarks() 
    {
        return marks;
    }

    public int getCorrectAnswerTot() 
    {
        return correctAnswerTot;
    }

    public int getWrongAnswerTot() 
    {
        return wrongAnswerTot;
    }

    public Date getSubmissionTime() 
    {
        return submissionTime;
    }

    public boolean isEvaluated() 
    {
        return isEvaluated; //returns true/false
    }

    @Override
    public String toString() 
    {
        return String.format("%s | Score: %d | Correct: %d | Wrong: %d", exam.getTitle(), marks, correctAnswerTot, wrongAnswerTot);
    }
} 