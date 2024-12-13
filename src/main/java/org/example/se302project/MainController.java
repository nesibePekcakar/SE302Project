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
import java.util.ArrayList;
import java.util.List;

public class MainController {
    @FXML
    private Label statusLabel;

    @FXML
    protected void onEnterAFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a CSV File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );

        Stage stage = new Stage();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {

            CSVReader csvReader = new CSVReader();

            if(selectedFile.getName().toLowerCase().contains("courses")){
                List<Course> courses = csvReader.readCourses(selectedFile.getAbsolutePath());
                csvReader.writeCoursesToFile(courses,true);
            }
            if(selectedFile.getName().toLowerCase().contains("classroomcapacity")){
                List<Classroom> classrooms = csvReader.readClassrooms(selectedFile.getAbsolutePath());
                csvReader.writeClassroomsToFile(classrooms,true);
            }

        } else {
            statusLabel.setText("No file selected.");
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
