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

    @FXML
    protected void onEnterAFile(ActionEvent event) {
        // Create a new FileChooser for selecting the courses file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select the CSV File for Courses");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );

        // Open file chooser dialog for courses
        Stage stage = new Stage();
        File selectedCoursesFile = fileChooser.showOpenDialog(stage);

        if (selectedCoursesFile != null) {
            // Create a new FileChooser for selecting the classroom capacity file
            fileChooser.setTitle("Select the CSV File for Classroom Capacities");

            // Open file chooser dialog for classrooms
            File selectedClassroomFile = fileChooser.showOpenDialog(stage);

            if (selectedClassroomFile != null) {
                // Instantiate CSVReader to read the files
                CSVReader csvReader = new CSVReader();

                // Process the selected courses file
                if (selectedCoursesFile.getName().toLowerCase().contains("courses")) {
                    List<Course> courses = csvReader.InitialReadCourses(selectedCoursesFile.getAbsolutePath());
                    csvReader.writeCoursesToFile(courses, true);  // Write courses to file
                }

                // Process the selected classroom capacity file
                if (selectedClassroomFile.getName().toLowerCase().contains("classroomcapacity")) {
                    List<Classroom> classrooms = csvReader.readClassrooms(selectedClassroomFile.getAbsolutePath());
                    csvReader.writeClassroomsToFile(classrooms, true);  // Write classrooms to file
                }
                // If both files are successfully processed, switch the scene
                switchToMatchingScene(event);

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

    @FXML

    protected void onHelp(ActionEvent event) {
        try {
            // Load the help-view.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("help-view.fxml"));
            Parent root = loader.load();

            // Set up the scene and stage
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
