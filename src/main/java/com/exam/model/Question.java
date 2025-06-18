package com.exam.model;

import java.util.ArrayList;
import java.util.List;

public class Question 
{
    private String givenQuestion;
    private List<String> options;
    private int correctAnswerIndex = -1;

    public Question(String givenQuestion) //constructor
    {
        this.givenQuestion = givenQuestion;
        this.options = new ArrayList<>();
    }

    public void setGivenQuestion(String givenQuestion) //setter 1
    {
        this.givenQuestion = givenQuestion;
    }

    public void addOption(String option) 
    {
        options.add(option); //each option is added to options ArrayList
    }

    public void setCorrectAnswer(int index) 
    {
        if (index >= 0 && index < options.size()) 
        {
            this.correctAnswerIndex = index;
        }
    }

    public String getGivenQuestion() //getter 1
    {
        return givenQuestion;
    }

    public List<String> getOptions() 
    {
        return options;
    }

    public int getCorrectAnswerIndex() 
    {
        return correctAnswerIndex;
    }

    public boolean isCorrectAnswer(int selectedAnswerIndex) 
    {
        return selectedAnswerIndex == correctAnswerIndex;
    }
} 