package com.exam.model;

public abstract class User {
    private String email;
    private String password;
    private String name;

    public User(String email, String password, String name) 
    {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public String getEmail() 
    {
        return this.email;
    }

    public String getPassword() 
    {
        return this.password;
    } 

    public String getName() 
    {
        return this.name;
    }

    public boolean checkPassword(String password) 
    {
        return this.getPassword().equals(password) && getPassword().length() >= 6;
    }
} 