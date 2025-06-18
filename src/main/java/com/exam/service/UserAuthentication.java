package com.exam.service;

import com.exam.model.Lecturer;
import com.exam.model.Student;
import com.exam.model.User;

import java.util.ArrayList;
import java.util.List;
//manages all users(students and lecs)
public class UserAuthentication 
{
    private List<User> users;        // list of User objects(stds&lecs) //instance variable is users
    private static UserAuthentication uniqueInstance; //static means this variable belongs to the class itself, not to any particular object

    private UserAuthentication() //This ensures no one can create new UserAuthentication() from outside the class.only 1 uniqueInstance var for the entire application. 
    {
        users = new ArrayList<>();
        // Add a default lecturer
        users.add(new Lecturer("lec1@exam.com", "pwd111", "John!"));
        users.add(new Lecturer("lec2@exam.com", "pwd222", "Ken!"));
        // Add some sample students
        users.add(new Student("std1@exam.com", "pwd123", "Mia"));
        users.add(new Student("std2@exam.com", "pwd321", "Bob")); 
    }

    public static UserAuthentication getUniqueInstance() //UserAuthentication is the return type//it returns an obj(stduent/lecturer)
    {
        if (uniqueInstance == null) //instance is a static reference to the one and only UserAuthentication object
        {
           uniqueInstance = new UserAuthentication(); //we're now creating UserAuthentication obj
        }
        return uniqueInstance; //returns the obj of UserAuthentication which contains stud&lecs.
    }
    
    /*If all three are correct — it returns that user.
    If no match is found — it returns null.*/
    public User login(String email, String password, String userType) 
    { 
        for (User user : users) 
        {
            if (user.getEmail().equals(email) && user.checkPassword(password)) 
            {
                if ((userType.equals("Lecturer") && user instanceof Lecturer) ||
                    (userType.equals("Student") && user instanceof Student)) 
                    {
                        return user;
                    }
            }
        }
        return null; // no user matched after checking all
    }

    public void addUser(User user) 
    {
        users.add(user); // adds the given User object to the 'users' list 
    }

    public List<User> getUsers() 
    {
        return users; // returns the entire Arraylist of users
    }
} 
