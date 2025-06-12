package com.exam.model;

import java.util.Date;

public class ExamResult {
    private Student student;
    private Exam exam;
    private int score;
    private int correctAnswers;
    private int wrongAnswers;
    private Date submissionTime;
    private boolean completed;

    public ExamResult(Student student, Exam exam) {
        this.student = student;
        this.exam = exam;
        this.score = 0;
        this.correctAnswers = 0;
        this.wrongAnswers = 0;
        this.submissionTime = new Date();
        this.completed = false;
    }

    public void calculateScore(int correctAnswers) {
        this.correctAnswers = correctAnswers;
        this.wrongAnswers = exam.getQuestions().size() - correctAnswers;
        this.score = (correctAnswers * 100) / exam.getQuestions().size();
        this.completed = true;
    }

    public Student getStudent() {
        return student;
    }

    public Exam getExam() {
        return exam;
    }

    public int getScore() {
        return score;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public int getWrongAnswers() {
        return wrongAnswers;
    }

    public Date getSubmissionTime() {
        return submissionTime;
    }

    public boolean isCompleted() {
        return completed;
    }

    @Override
    public String toString() {
        return String.format("%s | Score: %d | Correct: %d | Wrong: %d", exam.getTitle(), score, correctAnswers, wrongAnswers);
    }
} 