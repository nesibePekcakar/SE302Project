package org.example.se302project;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class DisplaySchedulesApp extends Application
{

    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.setTitle("Schedules Viewer");

        Button viewStudentSchedulesButton = new Button("View Student Schedules");
        Button viewClassroomSchedulesButton = new Button("View Classroom Schedules");

        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(15));
        mainLayout.getChildren().addAll(viewStudentSchedulesButton, viewClassroomSchedulesButton);

        viewStudentSchedulesButton.setOnAction(e -> showStudentSchedules(primaryStage));
        viewClassroomSchedulesButton.setOnAction(e -> showClassroomSchedules(primaryStage));

        Scene mainScene = new Scene(mainLayout, 400, 300);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private void showStudentSchedules(Stage primaryStage)
    {
        List<Student> students = CSVReader.InitialReadCourses("path_to_student_file.csv")
                .stream().map(course -> new Student(course.getStudents().get(0))) // Simplified
                .toList();

        ObservableList<String> studentSchedules = FXCollections.observableArrayList();
        for (Student student : students)
        {
            studentSchedules.add("Student: " + student.getName() + "\nClasses: " + student.getClasses());
        }

        ListView<String> studentScheduleListView = new ListView<>(studentSchedules);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> start(primaryStage));

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));
        layout.getChildren().addAll(studentScheduleListView, backButton);

        Scene studentScheduleScene = new Scene(layout, 400, 400);
        primaryStage.setScene(studentScheduleScene);
    }

    private void showClassroomSchedules(Stage primaryStage)
    {
        List<Classroom> classrooms = CSVReader.readClassrooms("path_to_classroom_file.csv");

        ObservableList<String> classroomSchedules = FXCollections.observableArrayList();
        for (Classroom classroom : classrooms)
        {
            classroomSchedules.add("Classroom: " + classroom.getClassroomName() + "\nCapacity: " + classroom.getCapacity());
        }

        TextArea classroomScheduleTextArea = new TextArea();
        classroomScheduleTextArea.setEditable(false);
        classroomScheduleTextArea.setText(String.join("\n", classroomSchedules));

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> start(primaryStage));

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));
        layout.getChildren().addAll(classroomScheduleTextArea, backButton);

        Scene classroomScheduleScene = new Scene(layout, 400, 400);
        primaryStage.setScene(classroomScheduleScene);
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
