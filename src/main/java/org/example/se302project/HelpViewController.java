package org.example.se302project;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class HelpViewController {
    @FXML
    private TextArea textArea; // The TextArea defined in your FXML file

    @FXML
    public void back() {
        try {
            // Load the FXML for the new scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent root = loader.load();

            // Get the current stage using the TextArea
            Stage stage = (Stage) textArea.getScene().getWindow();

            // Create a new scene and set it on the stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();}
            catch (IOException e) {
            showAlert("Error", "An error occurred while loading the previous screen: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
