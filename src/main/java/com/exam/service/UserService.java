package com.exam.service;

import com.exam.model.Lecturer;
import com.exam.model.Student;
import com.exam.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private List<User> users;
    private static UserService instance;

    private UserService() {
        users = new ArrayList<>();
        // Add a default lecturer
        users.add(new Lecturer("lecturer@example.com", "password123", "John Doe"));
        // Add some sample students
        users.add(new Student("student1@example.com", "password123", "Alice Smith"));
        users.add(new Student("student2@example.com", "password123", "Bob Johnson"));
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public User login(String email, String password, String userType) {
        for (User user : users) {
            if (user.getEmail().equals(email) && user.validatePassword(password)) {
                if ((userType.equals("Lecturer") && user instanceof Lecturer) ||
                    (userType.equals("Student") && user instanceof Student)) {
                    return user;
                }
            }
        }
        return null;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public List<User> getUsers() {
        return users;
    }
} 