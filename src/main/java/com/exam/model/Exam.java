package com.exam.model;

import java.util.ArrayList;
import java.util.List;

public class Exam 
{
    private String title;
    private List<Question> questions;
    private int timeLimitMins;
    private Lecturer examPreparedBy;

    public Exam(String title, Lecturer examPreparedBy) 
    {
        this.title = title;
        this.examPreparedBy = examPreparedBy;
        this.questions = new ArrayList<>();
        this.timeLimitMins = 30; // Default time limit
    }

    public void addQuestion(Question question) 
    {
        if (questions.size() <= 50) //can make upto 50 mcqs
        {
            questions.add(question); //adding each question into questions ArrayList
        }
    }

    public String getTitle() 
    {
        return title;
    }

    public List<Question> getQuestions() 
    {
        return questions;
    }

    public int getTimeLimitMins() 
    {
        return timeLimitMins;
    }

    public Lecturer getExamPreparedBy() 
    {
        return examPreparedBy;
    }

    @Override
    public String toString() 
    {
        return title;
    }
} 