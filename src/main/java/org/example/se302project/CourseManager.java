package org.example.se302project;
import java.io.*;
import java.util.*;

public class CourseManager {
    public CourseManager() {
    }

    // Method to read courses from file
    public static List<Course> ReadCourses(String filePath) {
        List<Course> courses = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                // Skip the header line
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                // Split the line into fields
                String[] fields = line.split(";");

                // Trim any extra spaces from the fields
                String courseName = fields[0].trim();
                String day = fields[1].trim();  // Day
                String startTime = fields[2].trim();  // Start time
                String durationStr = fields[3].trim();  // Duration
                String lecturer = fields[4].trim();  // Lecturer

                // Initialize list for students
                ArrayList<String> students = new ArrayList<>();

                // Start counting attendance (students)
                for (int i = 6; i < fields.length; i++) {
                    String studentName = fields[i].trim();

                    // Skip empty student names
                    if (!studentName.isEmpty()) {
                        students.add(studentName);
                    }
                }

                // Parse duration and handle any potential exceptions
                int duration = 0;
                try {
                    if (!durationStr.isEmpty()) {  // Check if durationStr is not empty
                        duration = Integer.parseInt(durationStr);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid duration format for course: " + courseName);
                }

                // Create and add the Course object to the list
                courses.add(new Course(courseName, day, startTime, duration, lecturer, students.size(), students));
            }

        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }

        return courses;
    }

    // Method to read classroom capacities from file
    public static List<Classroom> ReadClassrooms(String filePath) {
        List<Classroom> classrooms = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                // Skip the header line
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                // Split the line into fields
                String[] fields = line.split(";");

                // Trim any extra spaces from the fields
                String classroomName = fields[0].trim();
                int capacity = Integer.parseInt(fields[1].trim());  // Classroom capacity

                // Create and add the Classroom object to the list
                classrooms.add(new Classroom(classroomName, capacity));
            }

        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }

        return classrooms;
    }

    // Method to get the file path from the user (for courses)
    public static String getCoursesFilePath() {
        String userHome = System.getProperty("user.home");
        String appDirectory = userHome + "/SE302Project";

        // Ensure the directory exists
        File directory = new File(appDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String outputFilePath = appDirectory + "/Courses.csv";
        return outputFilePath;
    }


    // Method to get the file path from the user (for classroom capacities)
    public static String getClassroomCapacityFilePath() {
        String userHome = System.getProperty("user.home");
        String appDirectory = userHome + "/SE302Project";

        // Ensure the directory exists
        File directory = new File(appDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String outputFilePath = appDirectory + "/ClassroomCapacity.csv";
        return outputFilePath;
    }
    public static void writeRoomAssignmentsToCSV(Map<String, List<String>> roomAssignments, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Write the header row
            writer.write("Classroom Name;Assigned Courses\n");

            // Iterate through the roomAssignments map and write each entry to the file
            for (Map.Entry<String, List<String>> entry : roomAssignments.entrySet()) {
                String classroomName = entry.getKey();
                List<String> assignedCourses = entry.getValue();

                // Convert the list of assigned courses to a single string separated by commas
                String assignedCoursesString = String.join(", ", assignedCourses);

                // Write data in CSV format
                writer.write(classroomName + ";" + assignedCoursesString + "\n");
            }

            System.out.println("Room assignments have been successfully saved to: " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to the file: " + e.getMessage());
        }
    }

    // Method to get the file path from the user (for classroom capacities)
    public  String getMatchingFilePath() {
        String userHome = System.getProperty("user.home");
        String appDirectory = userHome + "/SE302Project";

        // Ensure the directory exists
        File directory = new File(appDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String outputFilePath = appDirectory + "/Matching.csv";
        return outputFilePath;
    }
    public  Map<String, List<String>> readMatching(String filePath) {
        Map<String, List<String>> roomAssignments = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Skip header row
            reader.readLine();

            // Read each line in the file
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");

                if (parts.length == 2) {
                    String classroomName = parts[0].trim();
                    String coursesString = parts[1].trim();

                    // Split the courses by commas and create a list
                    List<String> assignedCourses = Arrays.asList(coursesString.split(",\\s*"));

                    // Add to the map
                    roomAssignments.put(classroomName, assignedCourses);
                }
            }

            System.out.println("Room assignments have been successfully loaded from: " + filePath);

        } catch (IOException e) {
            System.err.println("Error reading from the file: " + e.getMessage());
        }

        return roomAssignments;
    }


}
