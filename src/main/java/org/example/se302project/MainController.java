package org.example.se302project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainController {
    @FXML
    private Label statusLabel;

    private List<Course> courses;
    private List<Classroom> classrooms;

    // Schedules for students and classrooms
    private Schedule studentSchedule = new Schedule();
    private Schedule classroomSchedule = new Schedule();

    @FXML
    protected void onEnterAFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select the CSV File for Courses");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );

        // Open file chooser dialog for courses
        Stage stage = new Stage();
        File selectedCoursesFile = fileChooser.showOpenDialog(stage);

        if (selectedCoursesFile != null) {
            fileChooser.setTitle("Select the CSV File for Classroom Capacities");

            File selectedClassroomFile = fileChooser.showOpenDialog(stage);

            if (selectedClassroomFile != null) {
                CSVReader csvReader = new CSVReader();

                // Process the selected courses file
                if (selectedCoursesFile.getName().toLowerCase().contains("courses")) {
                    courses = csvReader.InitialReadCourses(selectedCoursesFile.getAbsolutePath());
                }

                // Process the selected classroom capacity file
                if (selectedClassroomFile.getName().toLowerCase().contains("classroomcapacity")) {
                    classrooms = csvReader.readClassrooms(selectedClassroomFile.getAbsolutePath());
                }

                // Schedule the courses and classrooms
                scheduleCoursesAndClassrooms();

                // Switch to the schedule view scene
                switchToScheduleScene(event);

            } else {
                statusLabel.setText("No classroom capacity file selected.");
            }

        } else {
            statusLabel.setText("No courses file selected.");
        }
    }

    private void switchToMatchingScene(ActionEvent event) {
        // Load the new scene (matching scene) from an FXML file or create a new layout
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("matching-view.fxml"));
            Parent root = loader.load();


            // Set the new scene to the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));

            // Show the new scene
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading matching scene: " + e.getMessage());
        }
    }

    private void scheduleCoursesAndClassrooms() {
        // Logic to schedule courses for students and classrooms
        for (Course course : courses) {
            String courseTime = course.getDay() + " " + course.getStartTime();

            // Schedule each student for the course
            for (String student : course.getStudents()) {
                studentSchedule.addStudentSchedule(student, courseTime);
            }

            // Schedule each classroom for the course
            for (Classroom classroom : classrooms) {
                classroomSchedule.addClassroomSchedule(classroom.getClassroomName(), courseTime);
            }
        }
    }

    private void switchToScheduleScene(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("schedule-view.fxml"));
            Parent root = loader.load();

            // Pass the schedule data to the new scene
            ScheduleViewController scheduleViewController = loader.getController();
            scheduleViewController.setSchedule(studentSchedule, classroomSchedule);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));

            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading schedule scene: " + e.getMessage());
        }
    }

    @FXML
    protected void onHelp(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("help-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Help");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            if (statusLabel != null) {
                statusLabel.setText("Error loading Help page.");
            }
        }
    }

}
