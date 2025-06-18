package com.exam.model;

import java.util.ArrayList;
import java.util.List;

public class Lecturer extends User
 {
    private List<Exam> createdExams;

    public Lecturer(String email, String password, String name) 
    {
        super(email, password, name);
        this.createdExams = new ArrayList<>();
    }

    public void createExam(Exam exam) 
    {
        createdExams.add(exam);
    }

    public List<Exam> getCreatedExams() 
    {
        return createdExams;
    }
} 