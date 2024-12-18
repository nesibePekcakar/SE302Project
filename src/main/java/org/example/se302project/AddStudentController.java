package org.example.se302project;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class AddStudentController {

    @FXML
    private TextField studentNameField;

    @FXML
    private TextField courseNameField;

    @FXML
    private TextField hoursField;

    @FXML
    private Button addStudentButton;

    @FXML
    private Button resetButton;

    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        addStudentButton.setOnAction(e -> addStudent());
        resetButton.setOnAction(e -> resetFields());
        backButton.setOnAction(e -> goBack());
    }

    private void addStudent() {
        String studentName = studentNameField.getText();
        String courseName = courseNameField.getText();
        String hours = hoursField.getText();

        if (studentName.isEmpty() || courseName.isEmpty() || hours.isEmpty()) {
            showAlert("Error", "All fields must be filled out!", Alert.AlertType.ERROR);
            return;
        }

        try {
            int hoursInt = Integer.parseInt(hours);
            Student newStudent = new Student(studentName);
            Course newCourse = new Course(courseName, "", "", hoursInt, "", 0, new ArrayList<>());

            // Optionally add logic to enroll the student in the course

            showAlert("Success", "Student added successfully!", Alert.AlertType.INFORMATION);
            resetFields();
        } catch (NumberFormatException ex) {
            showAlert("Error", "Hours must be a valid number!", Alert.AlertType.ERROR);
        }
    }

    private void resetFields() {
        studentNameField.clear();
        courseNameField.clear();
        hoursField.clear();
    }

    private void goBack() {

        System.out.println("Navigating back...");
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
