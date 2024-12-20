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
    private TableColumn<Classroom, String> attendanceColumn;

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
            String classroomName = cellData.getValue().getClassroomName();
            List<String> assignedCourses = roomAssignments.get(classroomName);

            // Prepare the attendance information in a comma-separated format
            StringBuilder attendanceInfo = new StringBuilder();
            if (assignedCourses != null) {
                for (String courseName : assignedCourses) {
                    Course course = getCourseByName(courseName);
                    if (course != null) {
                        if (attendanceInfo.length() > 0) {
                            attendanceInfo.append(", ");
                        }
                        attendanceInfo.append(course.getAttendance());  // Append the attendance for each course
                    }
                }
            }

            // Return the comma-separated list of attendance values
            return new javafx.beans.property.SimpleStringProperty(attendanceInfo.toString());
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

    /*private void handleClassroomAssignmentEdit(Classroom classroom, String newCourseName) {
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

        // Get the current list of assigned courses for the classroom
        String classroomName = classroom.getClassroomName();
        List<String> assignedCourses = roomAssignments.getOrDefault(classroomName, new ArrayList<>());

        // Remove old course (if exists) from the list of assigned courses
        String oldCourseName = getAssignedCourseForClassroom(classroomName);
        if (!oldCourseName.equals("Empty")) {  // Only remove if the old course is not "Empty"
            assignedCourses.remove(oldCourseName);  // Directly modify the list
        }

        // Add new course to the list
        assignedCourses.add(newCourseName);

        // Update the roomAssignments map with the modified list
        roomAssignments.put(classroomName, assignedCourses);

        // Refresh the table to reflect the changes
        tableView.refresh();

        showAlert("Success", "Classroom assignment updated successfully.");
    }*/
    private void handleClassroomAssignmentEdit(Classroom classroom, String newCourseName) {
        Course newCourse = getCourseByName(newCourseName);
        if (newCourse == null) {
            showAlert("Error", "The specified course does not exist.");
            tableView.refresh();
            return;
        }

        // Check if the new course can fit in the classroom
        if (newCourse.getAttendance() > classroom.getCapacity()) {
            showAlert("Error", "Insufficient capacity: " +
                    "Classroom '" + classroom.getClassroomName() +
                    "' cannot accommodate " + newCourse.getAttendance() + " students.");
            tableView.refresh();
            return;
        }

        // Get the current list of assigned courses for the classroom
        List<String> assignedCourses = roomAssignments.getOrDefault(classroom.getClassroomName(), new ArrayList<>());

        // Step 1: Check for time conflicts with already assigned courses in the classroom
        boolean conflict = false;
        for (String assignedCourseName : assignedCourses) {
            Course assignedCourse = getCourseByName(assignedCourseName);
            if (assignedCourse != null && assignedCourse.getDay().equals(newCourse.getDay())) {
                // There is a time conflict if the courses overlap
                if (isTimeConflict(assignedCourse, newCourse)) {
                    conflict = true;
                    break;  // No need to check further if a conflict is found
                }
            }
        }

        if (conflict) {
            showAlert("Error", "The course " + newCourseName + " cannot be assigned to " + classroom.getClassroomName() + " due to a time conflict.");
            tableView.refresh();
            return;  // Exit if there's a time conflict
        }

        // Step 2: Find and remove the course from its current classroom (if already assigned)
        for (Classroom currentClassroom : classrooms) {
            // If the new course is already assigned to a classroom
            if (currentClassroom != classroom && currentClassroom.getClassroomName().equals(newCourse.getClassroom().getClassroomName())) {
                // Get the current list of courses assigned to the previous classroom
                List<String> currentAssignedCourses = roomAssignments.getOrDefault(currentClassroom.getClassroomName(), new ArrayList<>());
                currentAssignedCourses.remove(newCourse.getCourseName());  // Remove the old course from its current classroom
                roomAssignments.put(currentClassroom.getClassroomName(), currentAssignedCourses);  // Update the assignments
                break;  // Exit once we've removed the course from the old classroom
            }
        }

        // Step 3: Add the new course to the list of assigned courses for the new classroom
        assignedCourses.add(newCourseName);

        // Step 4: Update the roomAssignments map with the new list of assigned courses for the classroom
        roomAssignments.put(classroom.getClassroomName(), assignedCourses);

        // Refresh the table to reflect the changes
        tableView.refresh();

        showAlert("Success", "Classroom assignment updated successfully.");
    }

    private boolean isTimeConflict(Course course1, Course course2) {
        int startTime1 = convertTimeToMinutes(course1.getStartTime());
        int startTime2 = convertTimeToMinutes(course2.getStartTime());

        int duration1 = course1.getDurationInLectureHours() * 45; // Convert lecture hours to minutes
        int duration2 = course2.getDurationInLectureHours() * 45;

        int endTime1 = startTime1 + duration1;
        int endTime2 = startTime2 + duration2;

        return !(endTime1 <= startTime2 || endTime2 <= startTime1);  // Return true if there's a conflict
    }
    private int convertTimeToMinutes(String time) {
        // Split the time string into hours, minutes, and AM/PM parts
        String[] timeParts = time.split(":");

        // Get the hours and minutes parts from the time string
        int hours = Integer.parseInt(timeParts[0]);
        int minutes = Integer.parseInt(timeParts[1].substring(0, 2));
        String ampm = timeParts[1].substring(2); // AM or PM part

        // Adjust for 12-hour clock format
        if (ampm.equals("PM") && hours < 12) {
            hours += 12;  // Convert PM times (except 12 PM) to 24-hour format
        } else if (ampm.equals("AM") && hours == 12) {
            hours = 0;  // Convert 12 AM to 0 hours (midnight)
        }

        // Calculate the total time in minutes since midnight
        return hours * 60 + minutes;
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
