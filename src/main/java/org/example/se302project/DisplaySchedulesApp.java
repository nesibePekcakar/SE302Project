package org.example.se302project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DisplaySchedulesApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Schedules Viewer");

        // Load the matching-view.fxml as the main entry point
        try {
            Parent root = FXMLLoader.load(getClass().getResource("matching-view.fxml"));
            Scene mainScene = new Scene(root);
            primaryStage.setScene(mainScene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
