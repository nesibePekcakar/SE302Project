package org.example.se302project;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.se302project.ScheduleViewController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;



import java.util.List;

public class ScheduleViewController {

    @FXML
    private ListView<String> studentListView;
    @FXML
    private ListView<String> classroomListView;

    private Schedule studentSchedule = new Schedule();
    private Schedule classroomSchedule = new Schedule();
    CourseManager cm = new CourseManager();
    private List<Classroom> classrooms = cm.ReadClassrooms(cm.getClassroomCapacityFilePath());
    private List<Course> courses = cm.ReadCourses(cm.getCoursesFilePath());

    @FXML
    private MenuItem coursesAction1;
    @FXML
    private MenuItem studentsAction1;

    private VBox root;
    private boolean isInitialized= false;

    @FXML
    public void initialize() {
        if (!isInitialized) {
            isInitialized = true;
            loadData();
            configureMenuButtons(); // Call any initialization logic like setting up menu buttons
        }
    }
    private void loadData() {
        classrooms = cm.ReadClassrooms(cm.getClassroomCapacityFilePath());
        courses = cm.ReadCourses(cm.getCoursesFilePath());
    }


    public void setSchedule(Schedule studentSchedule, Schedule classroomSchedule) {
        this.studentSchedule = studentSchedule;
        this.classroomSchedule = classroomSchedule;

        // Populate the ListViews with the schedules
        populateScheduleListView();
    }

    private void populateScheduleListView() {
        scheduleCoursesAndClassrooms();

        // Populate the student schedule list view
        List<String> studentScheduledTimes = studentSchedule.getScheduledTimes();
        studentListView.getItems().addAll(studentScheduledTimes);

        // Populate the classroom schedule list view
        List<String> classroomScheduledTimes = classroomSchedule.getScheduledTimes();
        classroomListView.getItems().addAll(classroomScheduledTimes);
    }

    private void scheduleCoursesAndClassrooms() {
        // Logic to schedule courses for students and classrooms
        for (Course course : courses) {
            String courseTime = course.getDay() + " " + course.getStartTime();

            // Schedule each student for the course
            for (String student : course.getStudents()) {
                studentSchedule.addStudentSchedule(student, course.getCourseName(), courseTime);
            }

            // Schedule each classroom for the course
            for (Classroom classroom : classrooms) {
                classroomSchedule.addClassroomSchedule(classroom.getClassroomName(), courseTime);
            }
        }
    }

    private void configureMenuButtons() {
        coursesAction1.setOnAction(event -> showCourses());
        studentsAction1.setOnAction(event -> showStudents());
    }

    private void showCourses() {
        StringBuilder coursesList = new StringBuilder("Courses List:\n");
        for (Course course : courses) {
            coursesList.append(course.toString()).append("\n");
        }

        // Show the list in an alert dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Courses");
        alert.setHeaderText("All Courses");
        alert.setContentText(coursesList.toString());
        alert.showAndWait();
    }

    private void showStudents() {
        StringBuilder studentsList = new StringBuilder("Students List:\n");
        for (Course course : courses) {
            studentsList.append("Course: ").append(course.getCourseName()).append("\n");
            for (String student : course.getStudents()) {
                studentsList.append(" - ").append(student).append("\n");
            }
        }

        // Show the list in an alert dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Students");
        alert.setHeaderText("All Students");
        alert.setContentText(studentsList.toString());
        alert.showAndWait();
    }
}


