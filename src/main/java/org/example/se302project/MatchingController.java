package org.example.se302project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import java.io.IOException;
import java.io.FileWriter;
import java.io.IOException;
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

    private Stage stage;
    private Scene scene;

    @FXML
    private void viewSchedules(ActionEvent event) {
        try {
            // Load the view for student schedules
            Parent root = FXMLLoader.load(getClass().getResource("student-schedule-view.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // This method will be called when the "Edit Table" button is clicked
    public void editTable() {
        // Logic to allow the user to edit the table
        enableTableEditing();
    }

    // This method will be called when the "Download as File" button is clicked

    public void downloadAsFile() {
        // Create a FileChooser to allow the user to select a save location
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Table Data");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialFileName("classroom_assignments.csv");

        // Show the save file dialog
        java.io.File file = fileChooser.showSaveDialog(tableView.getScene().getWindow());
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                // Write the table data to the file
                writeTableDataToCSV(writer);
                showAlert("Download Successful", "The table data has been successfully saved to: " + file.getAbsolutePath());
            } catch (IOException e) {
                showAlert("Error", "An error occurred while saving the file: " + e.getMessage());
            }
        }
    }
    private void writeTableDataToCSV(FileWriter writer) throws IOException {
        // Write the header row
        writer.write("Classroom,Capacity,Attendance,Assigned Course\n");

        // Write each row of data
        for (Classroom classroom : tableView.getItems()) {
            String classroomName = classroom.getClassroomName();
            int capacity = classroom.getCapacity();
            int attendance = 0;

            // Get the assigned course name and its attendance
            String assignedCourse = getAssignedCourseForClassroom(classroomName);
            if (assignedCourse != null && !assignedCourse.equals("Empty")) {
                Course course = getCourseByName(assignedCourse);
                if (course != null) {
                    attendance = course.getAttendance();
                }
            }

            // Write data in CSV format
            writer.write(String.format("%s,%d,%d,%s\n", classroomName, capacity, attendance, assignedCourse));
        }
    }



    // This method will be called when the "Back" button is clicked
    public void back() {
        try {
            // Load the hello-view.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent root = loader.load();

            // Get the current stage (the window)
            Stage stage = (Stage) tableView.getScene().getWindow();

            // Set the new scene to the stage (this will show the hello-view.fxml)
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "An error occurred while loading the previous screen: " + e.getMessage());
        }
    }
    // new
    @FXML
    public void enableTableEditing() {
        // Ensure the column is editable
        assignedCourseColumn.setEditable(true);

        // Set cell factory to allow editing with text field
        assignedCourseColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        // Handle commit action
        assignedCourseColumn.setOnEditCommit(event -> {
            Classroom classroom = event.getRowValue();
            String newCourseName = event.getNewValue();
            handleClassroomAssignmentEdit(classroom, newCourseName);
        });

        // Enable editing on the table
        tableView.setEditable(true);

        // Notify user
        showAlert("Edit Enabled", "You can now edit lecture column directly.");
    }

    // new
    private void handleClassroomAssignmentEdit(Classroom classroom, String newCourseName) {
        // Find the course by name
        Course newCourse = getCourseByName(newCourseName);
        if (newCourse == null) {
            showAlert("Error", "The specified course does not exist.");
            tableView.refresh(); // Reset to avoid invalid data in the column
            return;
        }

        // Check if the classroom capacity can accommodate the course attendance
        if (newCourse.getAttendance() > classroom.getCapacity()) {
            showAlert("Error", "Insufficient capacity: " +
                    "Classroom '" + classroom.getClassroomName() +
                    "' cannot accommodate " + newCourse.getAttendance() + " students.");
            tableView.refresh(); // Reset to avoid invalid data in the table
            return; // Exit the method to prevent assignment
        }

        // Update the roomAssignments map
        String oldCourseName = getAssignedCourseForClassroom(classroom.getClassroomName());
        if (oldCourseName != null && !oldCourseName.equals("Empty")) {
            roomAssignments.remove(oldCourseName);
        }
        roomAssignments.put(newCourseName, classroom.getClassroomName());

        // Update classroom's assigned capacity dynamically
        classroom.setCapacity(newCourse.getAttendance());

        // Refresh the table to reflect the changes
        tableView.refresh();

        showAlert("Success", "Classroom assignment updated successfully.");
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
