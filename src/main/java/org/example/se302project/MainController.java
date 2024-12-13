package org.example.se302project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
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
}
