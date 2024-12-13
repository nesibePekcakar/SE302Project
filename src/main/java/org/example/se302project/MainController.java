package org.example.se302project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
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
            List<Course> courses = csvReader.readCourses(selectedFile.getAbsolutePath());

            // Define a consistent output file in the project directory
            String outputFilePath = "src/output_courses.csv";
            csvReader.writeCoursesToFile(courses, outputFilePath,true);

            statusLabel.setText("File processed successfully. Output written to: " + outputFilePath);
        } else {
            statusLabel.setText("No file selected.");
        }
    }
}
