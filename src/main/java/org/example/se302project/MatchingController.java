package org.example.se302project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Map;

public class MatchingController {

    @FXML
    private TableView<Classroom> tableView;

    @FXML
    private TableColumn<Classroom, String> classroomColumn;

    @FXML
    private TableColumn<Classroom, Integer> capacityColumn;

    @FXML
    private TableColumn<Classroom, Integer> attendanceColumn;

    @FXML
    private TableColumn<Classroom, String> assignedCourseColumn;  // Added column for showing assigned courses

    CourseManager cm = new CourseManager();
    private List<Classroom> classrooms= cm.ReadClassrooms(cm.getClassroomCapacityFilePath());
    private List<Course> courses= cm.ReadCourses(cm.getCoursesFilePath());
    RoomBooking rb = new RoomBooking(classrooms,courses);
    private Map<String, String> roomAssignments;



    @FXML
    public void initialize() {
        setData();
    }


    // Method to initialize the controller with the matched data
    public void setData() {
        roomAssignments = rb.assignRoomsToAllClasses();
        // Populate the table with the classroom data
        populateTable();
    }

    private void populateTable() {
        // Create an ObservableList for the classrooms
        ObservableList<Classroom> classroomList = FXCollections.observableArrayList(classrooms);

        // Set the classroom name and capacity columns
        classroomColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getClassroomName()));

        capacityColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getCapacity()).asObject());

        // Add the "Assigned Course" column to show matched course names
        assignedCourseColumn.setCellValueFactory(cellData -> {
            // For each classroom, get the assigned course from the roomAssignments map
            String classroomName = cellData.getValue().getClassroomName();
            String assignedCourse = getAssignedCourseForClassroom(classroomName);
            return new javafx.beans.property.SimpleStringProperty(assignedCourse);
        });
        // Set the attendance column to show attendance based on the associated Course
        attendanceColumn.setCellValueFactory(cellData -> {
            // Get the course assigned to the classroom (you may need to adjust this depending on your data model)
            String classroomName = cellData.getValue().getClassroomName();
            String assignedCourseName = getAssignedCourseForClassroom(classroomName);  // Get assigned course name

            // Now, find the Course object that matches the assigned course name
            Course assignedCourse = getCourseByName(assignedCourseName);

            // Return the attendance value from the Course object (assuming the Course class has an 'attendance' field)
            if (assignedCourse != null) {
                return new javafx.beans.property.SimpleIntegerProperty(assignedCourse.getAttendance()).asObject();
            } else {
                return new javafx.beans.property.SimpleIntegerProperty(0).asObject();  // Default if no course is assigned
            }
        });


        // Set the classroom data into the table
        tableView.setItems(classroomList);
    }

    // Helper method to get the Course object by course name
    private Course getCourseByName(String courseName) {
        for (Course course : courses) {
            if (course.getCourseName().equals(courseName)) {
                return course;
            }
        }
        return null;  // Return null if the course is not found
    }


    // Method to retrieve the assigned course for a given classroom
    private String getAssignedCourseForClassroom(String classroomName) {
        for (Map.Entry<String, String> entry : roomAssignments.entrySet()) {
            if (entry.getValue().equals(classroomName)) {
                return entry.getKey();  // Return the course name
            }
        }
        return "Empty";  // Return if no course is assigned
    }


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
