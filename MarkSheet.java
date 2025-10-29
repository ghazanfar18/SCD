package com.mycompany.marksheet;
import java.util.Scanner;
//2023F-BSE-103
public class MarkSheet {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Student Name: ");
        String studentName = scanner.nextLine();
        System.out.print("Enter Total Marks: ");
        int totalMarks = scanner.nextInt();
        System.out.print("Enter Obtained Marks: ");
        int obtainedMarks = scanner.nextInt();
        double percentage = (obtainedMarks * 100.0) / totalMarks;
        String grade;
        double gpa;

        if (percentage >= 90) {
            grade = "A+";
            gpa = 4.0;
        } else if (percentage >= 80) {
            grade = "A";
            gpa = 3.7;
        } else if (percentage >= 70) {
            grade = "B";
            gpa = 3.0;
        } else if (percentage >= 60) {
            grade = "C";
            gpa = 2.0;
        } else {
            grade = "F";
            gpa = 0.0;
        }

        System.out.println("\n--- Mark Sheet ---");
        System.out.println("Student Name: " + studentName);
        System.out.println("Total Marks: " + totalMarks);
        System.out.println("Obtained Marks: " + obtainedMarks);
        System.out.println("Percentage: " + String.format("%.2f", percentage) + "%");
        System.out.println("Grade: " + grade);
        System.out.println("GPA: " + gpa);}}
