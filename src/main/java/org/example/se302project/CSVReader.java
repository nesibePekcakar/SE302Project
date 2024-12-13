package org.example.se302project;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    public static void main(String[] args) {
        String coursesFilePath = "src/main/resources/org/example/se302project/ClassroomCapacity.cvs";
        String classroomsFilePath = "src/resources/ClassroomCapacity.csv";
        String outputFilePath = "src/main/resources/org/example/se302project/output.cvs";

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

        writeCoursesToFile(courses, outputFilePath, true);
    }

    /**
     * Reads courses from a CSV file.
     * @param filePath Path to the CSV file.
     * @return List of Course objects.
     */
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
            System.err.println("Error reading courses file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing number in courses file: " + e.getMessage());
        }
        return courses;
    }

    /**
     * Reads classroom information from a CSV file.
     * @param filePath Path to the CSV file.
     * @return List of ClassroomRead objects.
     */
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
            System.err.println("Error reading classrooms file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing number in classrooms file: " + e.getMessage());
        }
        return classrooms;
    }

    /**
     * Writes a list of courses to a CSV file.
     * @param courses List of courses to write.
     * @param outputFilePath Path to the output file.
     * @param append Whether to append to the file or overwrite it.
     */
    public static void writeCoursesToFile(List<Course> courses, String outputFilePath, boolean append) {
        File file = new File(outputFilePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, append))) {
            // Write header only if not appending and file does not already exist
            if (!append && !file.exists()) {
                writer.write("CourseName;StartTime;Duration;Lecturer;Capacity\n");
            }

            // Write each course
            for (Course course : courses) {
                writer.write(course.getCourseName() + ";" +
                        course.getStartTime() + ";" +
                        course.getDuration() + ";" +
                        course.getLecturer() + ";" +
                        course.getCapacity() + "\n");
            }

            System.out.println("Courses written to file: " + outputFilePath);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}