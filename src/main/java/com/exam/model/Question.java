package com.exam.model;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private String questionText;
    private List<String> options;
    private int correctOptionIndex = -1;

    public Question(String questionText) {
        this.questionText = questionText;
        this.options = new ArrayList<>();
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void addOption(String option) {
        options.add(option);
    }

    public void setCorrectOption(int index) {
        if (index >= 0 && index < options.size()) {
            this.correctOptionIndex = index;
        }
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }

    public boolean isCorrectAnswer(int selectedOptionIndex) {
        return selectedOptionIndex == correctOptionIndex;
    }
} 