package com.exam.model;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private List<ExamResult> examResults;

    public Student(String email, String password, String name) {
        super(email, password, name);
        this.examResults = new ArrayList<>();
    }

    public void addExamResult(ExamResult result) {
        examResults.add(result);
    }

    public List<ExamResult> getExamResults() {
        return examResults;
    }
} 