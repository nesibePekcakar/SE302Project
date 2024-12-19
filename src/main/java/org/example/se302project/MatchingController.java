package org.example.se302project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

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
    private TableColumn<Classroom, String> assignedCourseColumn;


    CourseManager cm = new CourseManager();
    private List<Classroom> classrooms = cm.ReadClassrooms(cm.getClassroomCapacityFilePath());
    private List<Course> courses = cm.ReadCourses(cm.getCoursesFilePath());

    RoomBooking rb = new RoomBooking(classrooms, courses);

    // Adjusted to store the assigned courses for each classroom
    private Map<String, List<String>> roomAssignments = new HashMap<>();

    @FXML
    public void initialize() {
        setData();
    }

    // Method to initialize the controller with the matched data
    public void setData() {
        roomAssignments = rb.assignRoomsToAllClasses(); // Assuming this now returns Map<String, List<String>>
        saveMatchingToCSV();
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
            // For each classroom, get the assigned courses from the roomAssignments map
            String classroomName = cellData.getValue().getClassroomName();
            List<String> assignedCourses = roomAssignments.get(classroomName);
            String assignedCourseNames = (assignedCourses != null && !assignedCourses.isEmpty())
                    ? String.join(", ", assignedCourses) : "Empty";
            return new javafx.beans.property.SimpleStringProperty(assignedCourseNames);
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
        List<String> assignedCourses = roomAssignments.get(classroomName);
        return (assignedCourses != null && !assignedCourses.isEmpty()) ? assignedCourses.get(0) : "Empty";
    }

    // This method will be called when the "Edit Table" button is clicked
    public void editTable() {
        enableTableEditing();
    }

    // This method will be called when the "Download as File" button is clicked
    public void downloadAsFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Table Data");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialFileName("classroom_assignments.csv");

        java.io.File file = fileChooser.showSaveDialog(tableView.getScene().getWindow());
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                writeTableDataToCSV(writer);
                showAlert("Download Successful", "The table data has been successfully saved to: " + file.getAbsolutePath());
            } catch (IOException e) {
                showAlert("Error", "An error occurred while saving the file: " + e.getMessage());
            }
        }
    }

    private void writeTableDataToCSV(FileWriter writer) throws IOException {
        // Write the header row
        writer.write("Classroom,Capacity,Attendance,Assigned Courses\n");

        // Write each row of data
        for (Classroom classroom : tableView.getItems()) {
            String classroomName = classroom.getClassroomName();
            int capacity = classroom.getCapacity();
            int attendance = 0;

            // Get the assigned courses for the classroom from roomAssignments
            List<String> assignedCourses = roomAssignments.get(classroomName);

            // If there are no assigned courses, set assignedCourses to an empty list
            if (assignedCourses == null) {
                assignedCourses = new ArrayList<>();
            }

            String assignedCourseNames = (assignedCourses.isEmpty())
                    ? "\"Empty\""
                    : "\"" + String.join(", ", assignedCourses) + "\"";

            // For the first course in the list (if exists), get the attendance value
            if (!assignedCourses.isEmpty()) {
                Course assignedCourse = getCourseByName(assignedCourses.get(0));
                if (assignedCourse != null) {
                    attendance = assignedCourse.getAttendance();
                }
            }

            // Write data in CSV format
            writer.write(String.format("%s,%d,%d,%s\n", classroomName, capacity, attendance, assignedCourseNames));
        }
    }




    // This method will be called when the "Back" button is clicked
    public void back() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tableView.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "An error occurred while loading the previous screen: " + e.getMessage());
        }
    }

    @FXML
    public void enableTableEditing() {
        assignedCourseColumn.setEditable(true);
        assignedCourseColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        assignedCourseColumn.setOnEditCommit(event -> {
            Classroom classroom = event.getRowValue();
            String newCourseName = event.getNewValue();
            handleClassroomAssignmentEdit(classroom, newCourseName);
        });

        tableView.setEditable(true);
        showAlert("Edit Enabled", "You can now edit the lecture column directly.");
    }

    private void handleClassroomAssignmentEdit(Classroom classroom, String newCourseName) {
        Course newCourse = getCourseByName(newCourseName);
        if (newCourse == null) {
            showAlert("Error", "The specified course does not exist.");
            tableView.refresh();
            return;
        }

        if (newCourse.getAttendance() > classroom.getCapacity()) {
            showAlert("Error", "Insufficient capacity: " +
                    "Classroom '" + classroom.getClassroomName() +
                    "' cannot accommodate " + newCourse.getAttendance() + " students.");
            tableView.refresh();
            return;
        }

        // Update the roomAssignments map
        String classroomName = classroom.getClassroomName();
        List<String> assignedCourses = roomAssignments.getOrDefault(classroomName, new ArrayList<>());

        // Remove old course if exists
        assignedCourses.removeIf(course -> course.equals(getAssignedCourseForClassroom(classroomName)));

        // Add new course
        assignedCourses.add(newCourseName);
        roomAssignments.put(classroomName, assignedCourses);

        // Refresh the table to reflect the changes
        tableView.refresh();

        showAlert("Success", "Classroom assignment updated successfully.");
    }

    // Method to retrieve the assigned course for a given classroom
    public void viewSchedules(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("schedule.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading matching scene: " + e.getMessage());
        }
    }

    public void saveMatchingToCSV() {
        String filePath = cm.getMatchingFilePath(); // You can change the file path or use a file chooser
        CourseManager.writeRoomAssignmentsToCSV(roomAssignments, filePath);
        showAlert("Save Successful", "Saved Successfully");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showHelp() {
        Alert helpAlert = new Alert(AlertType.INFORMATION);
        helpAlert.setTitle("Help");
        helpAlert.setHeaderText("Need Assistance?");
        Label label = new Label("This section allows you to see the general data gathered from csv files you have selected. "
                + "Here are the things you can do:\n"
                + "1. View schedules and arrange them\n"
                + "2. Edit the table on your behalf\n"
                + "3. Save the matching\n"
                + "4. Download the table in a place in your computer\n"
                + "5. Go back to the previous scene.");
        
        label.setWrapText(true);

        helpAlert.getDialogPane().setContent(label);

        helpAlert.getDialogPane().setPrefSize(500, 400);
        helpAlert.showAndWait();
    }
}
