package com.mycompany.quizapp;

import java.util.*;
import java.time.*;

public class Quizapp {
    static HashMap<String, String> teachers = new HashMap<>();
    static HashMap<String, String> students = new HashMap<>();
    static List<Question> quiz = new ArrayList<>();
    static List<StudentRecord> records = new ArrayList<>();
    static int quizTime = 30; // default time in minutes
    static Scanner sc = new Scanner(System.in);

    static class Question {
        String question;
        List<String> options;
        char correctAnswer;

        public Question(String question, List<String> options, char correctAnswer) {
            this.question = question;
            this.options = options;
            this.correctAnswer = correctAnswer;
        }
    }

    static class StudentRecord {
        String name;
        String section;
        int score;
        LocalDateTime dateTaken;
        Duration timeTaken;
        List<Character> studentAnswers;

        public StudentRecord(String name, String section, int score, Duration timeTaken, List<Character> studentAnswers) {
            this.name = name;
            this.section = section;
            this.score = score;
            this.dateTaken = LocalDateTime.now();
            this.timeTaken = timeTaken;
            this.studentAnswers = studentAnswers;
        }
    }

    // Helper method for non-empty input
    private static String getNonEmptyInput(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("This field cannot be empty! Please try again.");
            }
        } while (input.isEmpty());
        return input;
    }

    // Modified helper method for confirmation prompts
    private static boolean confirmAction(String action) {
        while (true) {
            System.out.print("\nAre you sure you want to " + action + "? (YES/NO): ");
            String input = sc.nextLine().trim().toUpperCase();
            if (input.equals("YES")) {
                return true;
            } else if (input.equals("NO")) {
                return false;
            } else if (input.isEmpty()) {
                System.out.println("This field cannot be empty! Please try again.");
            } else {
                System.out.println("Invalid input! Please enter YES or NO.");
            }
        }
    }

    public static void main(String[] args) {
        // Sample admin account
        teachers.put("admin", "admin123");
        
        while (true) {
            System.out.println("\n=== QUIZ SYSTEM ===");
            System.out.println("1. Teacher");
            System.out.println("2. Student");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");
            
            try {
                String input = sc.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println("Invalid choice! Please enter 1-3.");
                    continue;
                }
                int choice = Integer.parseInt(input);
                
                switch (choice) {
                    case 1:
                        teacherAuth();
                        break;
                    case 2:
                        studentAuth();
                        break;
                    case 3:
                        if(confirmAction("exit the program")) {
                            System.out.println("Exiting program...");
                            System.exit(0);
                        } else {
                            System.out.println("Exit cancelled.");
                        }
                        break;
                    default:
                        System.out.println("Invalid choice! Please enter 1-3.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }

    static void teacherAuth() {
        while (true) {
            System.out.println("\n=== TEACHER PORTAL ===");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Back to Main Menu");
            System.out.print("Choose option: ");
            
            try {
                String input = sc.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println("Invalid choice! Please enter 1-3.");
                    continue;
                }
                int choice = Integer.parseInt(input);
                
                switch (choice) {
                    case 1:
                        if (teacherLogin()) {
                            teacherMenu();
                            return;
                        }
                        break;
                    case 2:
                        teacherRegister();
                        break;
                    case 3:
                        if(confirmAction("return to main menu")) {
                            return;
                        }
                        break;
                    default:
                        System.out.println("Invalid choice! Please enter 1-3.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }

    static boolean teacherLogin() {
        String username = getNonEmptyInput("\nEnter username: ");
        String password = getNonEmptyInput("Enter password: ");
        
        if (teachers.containsKey(username) && teachers.get(username).equals(password)) {
            System.out.println("Login successful!");
            return true;
        } else {
            System.out.println("Invalid credentials! Please try again or register.");
            return false;
        }
    }

    static void teacherRegister() {
        String username;
        while (true) {
            username = getNonEmptyInput("\nEnter new username: ");
            if (!teachers.containsKey(username)) {
                break;
            }
            System.out.println("Username already exists! Please choose another.");
        }
        
        String password = getNonEmptyInput("Enter password: ");
        
        teachers.put(username, password);
        System.out.println("Registration successful! You can now login.");
    }

    static void teacherMenu() {
        while (true) {
            System.out.println("\n=== TEACHER MENU ===");
            System.out.println("1. Create/Update Quiz");
            System.out.println("2. View/Edit Questions");
            System.out.println("3. Set Quiz Time");
            System.out.println("4. View Student Records");
            System.out.println("5. Logout");
            System.out.print("Choose option: ");
            
            try {
                String input = sc.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println("Invalid choice! Please enter 1-5.");
                    continue;
                }
                int choice = Integer.parseInt(input);
                
                switch (choice) {
                    case 1:
                        createQuiz();
                        break;
                    case 2:
                        if (quiz.isEmpty()) {
                            System.out.print("\nHave you already created questions? (YES/NO): ");
                            String hasQuestions = sc.nextLine().trim().toUpperCase();
                            if (hasQuestions.equals("YES")) {
                                System.out.println("\nNo questions found. Please create questions first using option 1.");
                            } else {
                                System.out.println("\nPlease create questions first using option 1.");
                            }
                        } else {
                            manageQuestions();
                        }
                        break;
                    case 3:
                        setQuizTime();
                        break;
                    case 4:
                        viewRecords();
                        break;
                    case 5:
                        if(confirmAction("logout")) {
                            System.out.println("Logging out...");
                            return;
                        } else {
                            System.out.println("Logout cancelled.");
                        }
                        break;
                    default:
                        System.out.println("Invalid choice! Please enter 1-5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }

    static void createQuiz() {
        quiz.clear();
        System.out.println("\n=== CREATE QUIZ ===");
        
        while (true) {
            String input = getNonEmptyInput("Enter number of questions (1-50): ");
            try {
                int num = Integer.parseInt(input);
                
                if (num < 1 || num > 50) {
                    System.out.println("Please enter between 1-50 questions.");
                    continue;
                }
                
                for (int i = 0; i < num; i++) {
                    addQuestion(i+1);
                }
                
                System.out.println("\nQuiz created successfully with " + num + " questions!");
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }

    static void addQuestion(int qNum) {
        System.out.println("\n=== QUESTION " + qNum + " ===");
        
        String question = getNonEmptyInput("Enter question: ");
        
        List<String> options = new ArrayList<>();
        for (char c = 'A'; c <= 'D'; c++) {
            String option = getNonEmptyInput("Enter option " + c + ": ");
            options.add(option);
        }
        
        char correctAnswer;
        while (true) {
            String ans = getNonEmptyInput("Enter correct answer (A-D): ").toUpperCase();
            if (ans.length() == 1 && ans.charAt(0) >= 'A' && ans.charAt(0) <= 'D') {
                correctAnswer = ans.charAt(0);
                break;
            }
            System.out.println("Invalid answer! Please enter A-D.");
        }
        
        quiz.add(new Question(question, options, correctAnswer));
    }

    static void manageQuestions() {
        if (quiz.isEmpty()) {
            System.out.println("\nNo questions available! Please create a quiz first.");
            return;
        }
        
        while (true) {
            System.out.println("\n=== MANAGE QUESTIONS ===");
            System.out.println("Current number of questions: " + quiz.size());
            System.out.println("1. View All Questions");
            System.out.println("2. Add Questions");
            System.out.println("3. Edit Question");
            System.out.println("4. Delete Question");
            System.out.println("5. Back to Teacher Menu");
            System.out.print("Choose option: ");
            
            try {
                String input = sc.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println("Invalid choice! Please enter 1-5.");
                    continue;
                }
                int choice = Integer.parseInt(input);
                
                switch (choice) {
                    case 1:
                        viewAllQuestions();
                        break;
                    case 2:
                        String numInput = getNonEmptyInput("\nHow many questions would you like to add? (1-" + (50 - quiz.size()) + "): ");
                        try {
                            int numToAdd = Integer.parseInt(numInput);
                            if (numToAdd < 1 || numToAdd > (50 - quiz.size())) {
                                System.out.println("Invalid number! Please enter between 1-" + (50 - quiz.size()));
                                break;
                            }
                            for (int i = 0; i < numToAdd; i++) {
                                addQuestion(quiz.size()+1);
                            }
                            System.out.println(numToAdd + " questions added successfully!");
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input! Please enter a number.");
                        }
                        break;
                    case 3:
                        editQuestion();
                        break;
                    case 4:
                        deleteQuestion();
                        break;
                    case 5:
                        if(confirmAction("return to teacher menu")) {
                            return;
                        }
                        break;
                    default:
                        System.out.println("Invalid choice! Please enter 1-5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }

    static void viewAllQuestions() {
        System.out.println("\n=== ALL QUESTIONS ===");
        for (int i = 0; i < quiz.size(); i++) {
            Question q = quiz.get(i);
            System.out.println("\nQuestion " + (i+1) + ": " + q.question);
            char optionChar = 'A';
            for (String opt : q.options) {
                System.out.println("  " + optionChar + ") " + opt);
                optionChar++;
            }
            System.out.println("  Correct Answer: " + q.correctAnswer);
        }
    }

    static void editQuestion() {
        viewAllQuestions();
        
        String qNumInput = getNonEmptyInput("\nEnter question number to edit: ");
        try {
            int qNum = Integer.parseInt(qNumInput);
            
            if (qNum < 1 || qNum > quiz.size()) {
                System.out.println("Invalid question number!");
                return;
            }
            
            Question q = quiz.get(qNum-1);
            
            System.out.println("\nEditing Question " + qNum);
            System.out.println("Current question: " + q.question);
            String newQuestion = getNonEmptyInput("Enter new question: ");
            q.question = newQuestion;
            
            for (int i = 0; i < q.options.size(); i++) {
                System.out.println("Current option " + (char)('A'+i) + ": " + q.options.get(i));
                String newOption = getNonEmptyInput("Enter new option " + (char)('A'+i) + ": ");
                q.options.set(i, newOption);
            }
            
            System.out.println("Current correct answer: " + q.correctAnswer);
            while (true) {
                String newAns = getNonEmptyInput("Enter new correct answer (A-D): ").toUpperCase();
                if (newAns.length() == 1 && newAns.charAt(0) >= 'A' && newAns.charAt(0) <= 'D') {
                    q.correctAnswer = newAns.charAt(0);
                    break;
                }
                System.out.println("Invalid answer! Please enter A-D.");
            }
            
            System.out.println("Question updated successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a number.");
        }
    }

    static void deleteQuestion() {
        viewAllQuestions();
        
        String qNumInput = getNonEmptyInput("\nEnter question number to delete: ");
        try {
            int qNum = Integer.parseInt(qNumInput);
            
            if (qNum < 1 || qNum > quiz.size()) {
                System.out.println("Invalid question number!");
                return;
            }
            
            if(confirmAction("delete this question")) {
                quiz.remove(qNum-1);
                System.out.println("Question deleted successfully!");
            } else {
                System.out.println("Question deletion cancelled.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a number.");
        }
    }

    static void setQuizTime() {
        System.out.println("\nCurrent quiz time: " + quizTime + " minutes");
        String timeInput = getNonEmptyInput("Enter new quiz time in minutes (1-180): ");
        
        try {
            int time = Integer.parseInt(timeInput);
            
            if (time < 1 || time > 180) {
                System.out.println("Please enter between 1-180 minutes.");
                return;
            }
            
            quizTime = time;
            System.out.println("Quiz time updated successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a number.");
        }
    }

    static void viewRecords() {
        if (records.isEmpty()) {
            System.out.println("\nNo student records available yet.");
            return;
        }
        
        System.out.println("\n=== STUDENT RECORDS ===");
        System.out.printf("%-20s %-15s %-10s %-15s %-20s\n", "Name", "Section", "Score", "Time Taken", "Date Taken");
        for (StudentRecord record : records) {
            System.out.printf("%-20s %-15s %-10d %-15s %-20s\n", 
                record.name, 
                record.section, 
                record.score,
                String.format("%d min %d sec", 
                    record.timeTaken.toMinutesPart(),
                    record.timeTaken.toSecondsPart()),
                record.dateTaken.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        }
        
        System.out.println("\nTotal students taken: " + records.size());
        if (!quiz.isEmpty()) {
            double avgScore = records.stream().mapToInt(r -> r.score).average().orElse(0);
            System.out.printf("Average score: %.1f/%d\n", avgScore, quiz.size());
            
            double avgSeconds = records.stream()
                .mapToLong(r -> r.timeTaken.getSeconds())
                .average()
                .orElse(0);
            System.out.printf("Average time taken: %d min %d sec\n", 
                (long)(avgSeconds / 60), 
                (long)(avgSeconds % 60));
        }
    }

    static void studentAuth() {
        while (true) {
            System.out.println("\n=== STUDENT PORTAL ===");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Back to Main Menu");
            System.out.print("Choose option: ");
            
            try {
                String input = sc.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println("Invalid choice! Please enter 1-3.");
                    continue;
                }
                int choice = Integer.parseInt(input);
                
                switch (choice) {
                    case 1:
                        if (studentLogin()) {
                            studentQuiz();
                            return;
                        }
                        break;
                    case 2:
                        studentRegister();
                        break;
                    case 3:
                        if(confirmAction("return to main menu")) {
                            return;
                        }
                        break;
                    default:
                        System.out.println("Invalid choice! Please enter 1-3.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }

    static boolean studentLogin() {
        String username = getNonEmptyInput("\nEnter username: ");
        String password = getNonEmptyInput("Enter password: ");
        
        if (students.containsKey(username) && students.get(username).equals(password)) {
            System.out.println("Login successful!");
            return true;
        } else {
            System.out.println("Invalid credentials! Please try again or register.");
            return false;
        }
    }

    static void studentRegister() {
        String username;
        while (true) {
            username = getNonEmptyInput("\nEnter new username: ");
            if (!students.containsKey(username)) {
                break;
            }
            System.out.println("Username already exists! Please choose another.");
        }
        
        String password = getNonEmptyInput("Enter password: ");
        
        students.put(username, password);
        System.out.println("Registration successful! You can now login.");
    }

    static void studentQuiz() {
        if (quiz.isEmpty()) {
            System.out.println("\nNo quiz available yet. Please wait for the teacher to create one.");
            return;
        }
        
        System.out.println("\n=== QUIZ DETAILS ===");
        String name = getNonEmptyInput("Enter your full name: ");
        String section = getNonEmptyInput("Enter your section: ");
        
        System.out.println("\n=== QUIZ INSTRUCTIONS ===");
        System.out.println("1. You have " + quizTime + " minutes to complete the quiz");
        System.out.println("2. There are " + quiz.size() + " questions");
        System.out.println("3. Each question has 4 options (A-D)");
        System.out.println("4. Enter your answer as A, B, C, or D");
        System.out.println("\nPress Enter to start the quiz...");
        sc.nextLine();
        
        Instant startTime = Instant.now();
        Instant endTime = startTime.plus(Duration.ofMinutes(quizTime));
        int score = 0;
        List<Character> studentAnswers = new ArrayList<>();
        
        for (int i = 0; i < quiz.size(); i++) {
            Question q = quiz.get(i);
            
            if (Instant.now().isAfter(endTime)) {
                System.out.println("\nTime's up! Quiz submitted automatically.");
                break;
            }
            
            Duration remaining = Duration.between(Instant.now(), endTime);
            long mins = remaining.toMinutes();
            long secs = remaining.minusMinutes(mins).getSeconds();
            
            System.out.println("\nQuestion " + (i+1) + "/" + quiz.size() + 
                             " (Time left: " + mins + "m " + secs + "s)");
            System.out.println(q.question);
            
            char optionChar = 'A';
            for (String opt : q.options) {
                System.out.println(optionChar + ") " + opt);
                optionChar++;
            }
            
            while (true) {
                String answer = getNonEmptyInput("Your answer (A-D): ").toUpperCase();
                
                if (answer.length() == 1 && answer.charAt(0) >= 'A' && answer.charAt(0) <= 'D') {
                    studentAnswers.add(answer.charAt(0));
                    if (answer.charAt(0) == q.correctAnswer) {
                        score++;
                    }
                    break;
                }
                System.out.println("Invalid input! Please enter A, B, C, or D.");
            }
        }
        
        Duration timeTaken = Duration.between(startTime, Instant.now());
        if (timeTaken.compareTo(Duration.ofMinutes(quizTime)) > 0) {
            timeTaken = Duration.ofMinutes(quizTime);
        }
        
        records.add(new StudentRecord(name, section, score, timeTaken, studentAnswers));
        
        System.out.println("\n=== QUIZ COMPLETED ===");
        System.out.println("Your score: " + score + "/" + quiz.size());
        System.out.printf("Time taken: %d min %d sec\n", 
            timeTaken.toMinutesPart(), 
            timeTaken.toSecondsPart());
        
        while (true) {
            System.out.println("\n1. View Score and Answers");
            System.out.println("2. Back to Main Menu");
            System.out.print("Choose option: ");
            
            try {
                String input = sc.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println("Invalid choice! Please enter 1 or 2.");
                    continue;
                }
                int choice = Integer.parseInt(input);
                
                if (choice == 1) {
                    System.out.println("\n=== YOUR QUIZ RESULTS ===");
                    System.out.println("Name: " + name);
                    System.out.println("Section: " + section);
                    System.out.println("Score: " + score + "/" + quiz.size());
                    System.out.printf("Time taken: %d min %d sec\n", 
                        timeTaken.toMinutesPart(), 
                        timeTaken.toSecondsPart());
                    
                    System.out.println("\n=== QUESTIONS AND ANSWERS ===");
                    for (int i = 0; i < quiz.size(); i++) {
                        Question q = quiz.get(i);
                        System.out.println("\nQuestion " + (i+1) + ": " + q.question);
                        char optionChar = 'A';
                        for (String opt : q.options) {
                            System.out.println("  " + optionChar + ") " + opt);
                            optionChar++;
                        }
                        System.out.println("  Correct Answer: " + q.correctAnswer);
                        System.out.println("  Your Answer: " + 
                            (i < studentAnswers.size() ? studentAnswers.get(i) : "Not answered"));
                        System.out.println("  Result: " + 
                            (i < studentAnswers.size() && studentAnswers.get(i) == q.correctAnswer ? 
                            "CORRECT" : "WRONG"));
                    }
                } else if (choice == 2) {
                    if(confirmAction("return to main menu")) {
                        return;
                    }
                } else {
                    System.out.println("Invalid choice! Please enter 1 or 2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }
}