package org.example.se302project;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MatchingController {

    @FXML
    private TableView<Classroom> tableView;

    @FXML
    private TableColumn<Classroom, String> classroomColumn;

    @FXML
    private TableColumn<Classroom, Integer> capacityColumn;

    // This method will be called when the "View Schedules" button is clicked
    public void viewSchedules() {
        // Logic to navigate to a new scene for viewing schedules
        showAlert("View Schedules", "This button will show schedules.");
    }

    // This method will be called when the "Edit Table" button is clicked
    public void editTable() {
        // Logic to allow the user to edit the table
        showAlert("Edit Table", "This button will allow you to edit the table.");
    }

    // This method will be called when the "Download as File" button is clicked
    public void downloadAsFile() {
        // Logic to download the table data as a file
        showAlert("Download File", "This button will allow you to download the table data.");
    }

    // This method will be called when the "Back" button is clicked
    public void back() {
        // Logic to go back to the previous screen
        showAlert("Back", "This button will take you back to the previous screen.");
    }

    // Helper method to display a simple alert box for testing the button actions
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
