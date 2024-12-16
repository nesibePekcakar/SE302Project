package org.example.se302project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.util.List;

public class ScheduleViewController {

    @FXML
    private TableView<Classroom> classroomTableView;

    @FXML
    private TableColumn<Classroom, String> classroomColumn;

    @FXML
    private TableColumn<Classroom, Integer> capacityColumn;

    @FXML
    private TableColumn<Classroom, String> assignedCourseColumn;

    @FXML
    private TableColumn<Classroom, Integer> attendanceColumn;

    private List<Classroom> classrooms;

    public void initialize() {
        // Get classrooms data from the existing CourseManager logic
        CourseManager cm = new CourseManager();
        classrooms = cm.ReadClassrooms(cm.getClassroomCapacityFilePath());

        // Populate the table
        ObservableList<Classroom> classroomList = FXCollections.observableArrayList(classrooms);
        classroomTableView.setItems(classroomList);

        // Set up the columns
        classroomColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getClassroomName()));
        capacityColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getCapacity()).asObject());

        assignedCourseColumn.setCellValueFactory(cellData -> {
            String assignedCourse = getAssignedCourseForClassroom(cellData.getValue().getClassroomName());
            return new javafx.beans.property.SimpleStringProperty(assignedCourse);
        });

        attendanceColumn.setCellValueFactory(cellData -> {
            String assignedCourseName = getAssignedCourseForClassroom(cellData.getValue().getClassroomName());
            Course assignedCourse = getCourseByName(assignedCourseName);
            return new javafx.beans.property.SimpleIntegerProperty(assignedCourse != null ? assignedCourse.getAttendance() : 0).asObject();
        });
    }

    // Helper methods to get assigned course and course details
    private String getAssignedCourseForClassroom(String classroomName) {
        // Implement similar to your MatchingController's logic
        return "CourseName";  // Example placeholder
    }

    private List<Course> courses = CSVReader.InitialReadCourses("path/to/your/courses.csv");

    private Course getCourseByName(String courseName) {
        // Loop through the list of courses and return the matching course
        for (Course course : courses) {
            if (course.getCourseName().equals(courseName)) {
                return course;
            }
        }
        return null;  // Return null if no matching course is found
    }


    // Back button to close this window
    @FXML
    public void goBack() {
        Stage stage = (Stage) classroomTableView.getScene().getWindow();
        stage.close();
    }

    // Helper method to show alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
