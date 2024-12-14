package org.example.se302project;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    public CSVReader() {
    }

    /**
     * Reads course information from a CSV file.
     * @param filePath Path to the CSV file.
     * @return List of Course objects.
     */
    public static List<Course> InitialReadCourses(String filePath) {
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
                String dayNstartTime = fields[1].trim();
                String durationStr = fields[2].trim();
                String lecturer = fields[3].trim();
                String[] dayNstartTimeArr = dayNstartTime.split(" ");

                // Initialize list for students
                ArrayList<String> students = new ArrayList<>();

                // Start counting attendance (students)
                for (int i = 5; i < fields.length; i++) {
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
                courses.add(new Course(courseName, dayNstartTimeArr[0], dayNstartTimeArr[1], duration, lecturer, students.size(), students));
            }

        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }

        return courses;
    }

    /**
     * Reads classroom information from a CSV file.
     * @param filePath Path to the CSV file.
     * @return List of Classroom objects.
     */
    public static List<Classroom> readClassrooms(String filePath) {
        List<Classroom> classrooms = new ArrayList<>();
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

                String classroomName = fields[0].trim();
                int capacity = Integer.parseInt(fields[1].trim());

                classrooms.add(new Classroom(classroomName, capacity));
            }
        } catch (IOException e) {
            System.err.println("Error reading classrooms file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing number in classrooms file: " + e.getMessage());
        }
        return classrooms;
    }

    /**
     * Writes a list of courses to a CSV file.
     * @param courses List of courses to write.
     * @param append Whether to append to the file or overwrite it.
     */
    public static void writeCoursesToFile(List<Course> courses, boolean append) {
        String userHome = System.getProperty("user.home");
        String appDirectory = userHome + "/SE302Project";

        // Ensure the directory exists
        File directory = new File(appDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String outputFilePath = appDirectory + "/Courses.csv";
        File file = new File(outputFilePath);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Existing file deleted: " + outputFilePath);
            } else {
                System.err.println("Failed to delete the existing file.");
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, append))) {
            // Write header only if not appending and file does not already exist
            if (!append && !file.exists()) {
                writer.write("Course;Day;TimeToStart;DurationInLectureHours;Lecturer;Students\n");
            }

            // Write each course
            for (Course course : courses) {
                String students = String.join(";", course.getStudents());
                writer.write(course.getCourseName() + ";" +
                        course.getDay() + ";" +
                        course.getStartTime() + ";" +
                        course.getDurationInLectureHours() + ";" +
                        course.getLecturer() + ";" +
                        course.getAttendance() + ";" +
                        students + "\n");
            }

            System.out.println("Courses written to file: " + outputFilePath);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Writes classroom information to a CSV file.
     * @param classrooms List of classrooms to write.
     * @param append Whether to append to the file or overwrite it.
     */
    public static void writeClassroomsToFile(List<Classroom> classrooms, boolean append) {
        String userHome = System.getProperty("user.home");
        String appDirectory = userHome + "/SE302Project";

        // Ensure the directory exists
        File directory = new File(appDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String outputFilePath = appDirectory + "/ClassroomCapacity.csv";
        File file = new File(outputFilePath);

        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Existing file deleted: " + outputFilePath);
            } else {
                System.err.println("Failed to delete the existing file.");
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, append))) {
            // Write header only if not appending and file does not already exist
            if (!append && !file.exists()) {
                writer.write("ClassroomName;Capacity\n");
            }

            // Write each classroom
            for (Classroom classroom : classrooms) {
                writer.write(classroom.getClassroomName() + ";" +
                        classroom.getCapacity() + "\n");
            }

            System.out.println("Classrooms written to file: " + outputFilePath);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}
