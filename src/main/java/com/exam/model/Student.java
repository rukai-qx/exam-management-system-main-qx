package com.exam.model;

import java.util.ArrayList;
import java.util.List;

public class Student extends User 
{
    //ecncapsulation
    private List<ExamResult> resultsInfo;//private instance variable in students class//means resultInfo can only be accessed within this class only.
                                        //all student's resultinfo (examResults) is in ExamResult List.
    public Student(String email, String password, String name) 
    {
        super(email, password, name);
        this.resultsInfo = new ArrayList<>();
    }

    public void addExamResult(ExamResult addResult) 
    {
        resultsInfo.add(addResult); //adding each student's result to the resultInfo List
    }

    public List<ExamResult> getResultsInfo() 
    {
        return resultsInfo;
    }
} 
//List<ExamResult> : ExamResult List holds objects of ExamResult class