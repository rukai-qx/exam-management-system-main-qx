package com.exam.model;

import java.util.ArrayList;
import java.util.List;

public class Exam {
    private String title;
    private List<Question> questions;
    private int timeLimitMinutes;
    private Lecturer createdBy;

    public Exam(String title, Lecturer createdBy) {
        this.title = title;
        this.createdBy = createdBy;
        this.questions = new ArrayList<>();
        this.timeLimitMinutes = 30; // Default time limit
    }

    public void addQuestion(Question question) {
        if (questions.size() < 50) {
            questions.add(question);
        }
    }

    public String getTitle() {
        return title;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public int getTimeLimitMinutes() {
        return timeLimitMinutes;
    }

    public Lecturer getCreatedBy() {
        return createdBy;
    }

    @Override
    public String toString() {
        return title;
    }
} 