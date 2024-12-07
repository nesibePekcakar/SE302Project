package org.example.se302project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    public static void main(String[] args) {
        String coursesFilePath = "src/Courses.csv";
        String classroomsFilePath = "src/ClassroomCapacity.csv";

        List<Course> courses = readCourses(coursesFilePath);
        List<ClassroomRead> classrooms = readClassrooms(classroomsFilePath);

        System.out.println("Courses:");
        for (Course course : courses) {
            System.out.println(course);
        }

        System.out.println("\nClassrooms:");
        for (ClassroomRead classroom : classrooms) {
            System.out.println(classroom);
        }
    }

    public static List<Course> readCourses(String filePath) {
        List<Course> courses = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] fields = line.split(";");
                if (fields.length < 5) {
                    System.out.println("Skipping invalid line: " + line);
                    continue;
                }

                String courseName = fields[0];
                String startTime = fields[1];
                int duration = Integer.parseInt(fields[2].trim());
                String lecturer = fields[3];

                courses.add(new Course(courseName, startTime, duration, lecturer, 0));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Error parsing number: " + e.getMessage());
        }
        return courses;
    }

    public static List<ClassroomRead> readClassrooms(String filePath) {
        List<ClassroomRead> classrooms = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] fields = line.split(";");
                if (fields.length < 2) {
                    System.out.println("Skipping invalid line: " + line);
                    continue;
                }

                String classroomName = fields[0];
                int capacity = Integer.parseInt(fields[1].trim());

                classrooms.add(new ClassroomRead(classroomName, capacity));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Error parsing number: " + e.getMessage());
        }
        return classrooms;
    }

}

