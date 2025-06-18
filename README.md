# Exam Management System

A Java Swing application for managing exams, allowing lecturers to create exams and students to take them.

## Features

- Separate login for lecturers and students
- Email and password validation
- Lecturer features:
  - Create exams with multiple-choice questions
  - Set correct answers for each question
  - View created exams
- Student features:
  - View available exams
  - Take exams with a 30-minute time limit
  - View exam results
- Automatic exam submission when time expires
- Logout functionality

## Default Users

### Lecturer
- Email: lec1@exam.com
- Password: pwd111
- Email: lec2@exam.com
- Password: pwd222

### Students
- Email: std1@exam.com
- Password: pwd123
- Email: std2@exam.com
- Password: pwd321

## How to Run

1. Make sure you have Java JDK 8 or higher installed
2. Compile the project:
   ```bash
   javac -d out src/main/java/com/exam/*.java src/main/java/com/exam/*/*.java
   ```
3. Run the application:
   ```bash
   java -cp out com.exam.ExamManagementSystem
   ```

## Usage

### Lecturer
1. Log in using the lecturer email & password
2. Click "Create New Exam" to create an exam
3. Add questions and their answers
4. Select the correct answer for each question
5. Save the exam

### Student
1. Log in using student email & password
2. View available exams in the left panel
3. Select an exam and click "Do the Selected Exam"
4. Answer the questions within the 30-minute time limit
5. Click "Submit Exam" when done or wait for automatic submission
6. View results in the right panel

## Project Structure

```
src/main/java/com/exam/
├── ExamManagementSystem.java
├── model/
│   ├── User.java
│   ├── Student.java
│   ├── Lecturer.java
│   ├── Exam.java
│   ├── Question.java
│   └── ExamResult.java
├── service/
│   └── UserAuthentication.java
└── ui/
    ├── LoginFrame.java
    ├── LecturerHub.java
    └── StudentHub.java
``` 
