package org.example.se302project;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.*;

public class ScheduleViewController {

    @FXML
    private ChoiceBox<String> classroomsChoiceBox; // New choice box for classrooms

    @FXML
    private ChoiceBox<String> studentsChoiceBox;

    @FXML
    private TableView<WeeklySchedule> scheduleTableView;

    @FXML
    private TableColumn<WeeklySchedule, String> mondayColumn;

    @FXML
    private TableColumn<WeeklySchedule, String> tuesdayColumn;

    @FXML
    private TableColumn<WeeklySchedule, String> wednesdayColumn;

    @FXML
    private TableColumn<WeeklySchedule, String> thursdayColumn;

    @FXML
    private TableColumn<WeeklySchedule, String> fridayColumn;

    private CourseManager cm = new CourseManager();
    private List<Classroom> classrooms = cm.ReadClassrooms(cm.getClassroomCapacityFilePath());
    private List<Course> courses = cm.ReadCourses(cm.getCoursesFilePath());
    private Map<String, List<String>> matching = cm.readMatching(cm.getMatchingFilePath());

    @FXML
    public void initialize() {
        populateChoiceBoxes();
        setupScheduleTable();
    }

    private void populateChoiceBoxes() {
        classroomsChoiceBox.getItems().clear();
        for (Classroom classroom : classrooms) {
            classroomsChoiceBox.getItems().add(classroom.getClassroomName());
        }

        // Classroom selection handler
        classroomsChoiceBox.setOnAction(e -> {
            String selectedClassroom = classroomsChoiceBox.getValue();
            System.out.println("Selected Classroom: " + selectedClassroom);
            populateScheduleTable(selectedClassroom); // Update the table with classroom schedule
        });

        // Populate Students ChoiceBox
        studentsChoiceBox.getItems().clear();
        for (String student : getAllStudentNames()) {
            studentsChoiceBox.getItems().add(student);
        }

        // Student selection handler
        studentsChoiceBox.setOnAction(e -> {
            String selectedStudent = studentsChoiceBox.getValue();
            System.out.println("Selected Student: " + selectedStudent);
            populateScheduleTable(selectedStudent); // Update the table with student schedule
        });
    }

    private void setupScheduleTable() {
        mondayColumn.setCellValueFactory(new PropertyValueFactory<>("monday"));
        tuesdayColumn.setCellValueFactory(new PropertyValueFactory<>("tuesday"));
        wednesdayColumn.setCellValueFactory(new PropertyValueFactory<>("wednesday"));
        thursdayColumn.setCellValueFactory(new PropertyValueFactory<>("thursday"));
        fridayColumn.setCellValueFactory(new PropertyValueFactory<>("friday"));
    }

    private void populateScheduleTable(String selection) {
        scheduleTableView.getItems().clear();
        if (studentsChoiceBox.getValue() != null && studentsChoiceBox.getValue().equals(selection)) {
            // If the selected item is a student, create and display their weekly schedule
            WeeklySchedule studentWeeklySchedule = CreateStudentSchedule(selection);
            scheduleTableView.getItems().add(studentWeeklySchedule);
        } else if (classroomsChoiceBox.getValue() != null && classroomsChoiceBox.getValue().equals(selection)) {
            // If the selected item is a classroom, retrieve and display its weekly schedule
            List<Course> classroomCourses = getClassroomClasses(selection);
            for (Course course : classroomCourses) {
                WeeklySchedule classroomWeeklySchedule = new WeeklySchedule(
                        course.getDay().equals("Monday") ? course.getCourseName() : "",
                        course.getDay().equals("Tuesday") ? course.getCourseName() : "",
                        course.getDay().equals("Wednesday") ? course.getCourseName() : "",
                        course.getDay().equals("Thursday") ? course.getCourseName() : "",
                        course.getDay().equals("Friday") ? course.getCourseName() : ""
                );
                scheduleTableView.getItems().add(classroomWeeklySchedule);
            }
        }
    }

    private List<String> getAllStudentNames() {
        Set<String> uniqueStudents = new HashSet<>(); // Use a Set to avoid duplicates
        for (Course course : courses) {
            uniqueStudents.addAll(course.getStudents());
        }
        return new ArrayList<>(uniqueStudents); // Convert back to a List
    }

    private WeeklySchedule CreateStudentSchedule(String studentName) {
        // Retrieve the list of courses the student is enrolled in
        List<Course> studentClasses = getStudentClasses(studentName);
        String monday = "", tuesday = "", wednesday = "", thursday = "", friday = "";

        // Loop through the classes to assign them to the correct day
        for (Course classObj : studentClasses) {
            String classDay = classObj.getDay();
            String className = classObj.getCourseName();

            // Assign the class to the appropriate day
            switch (classDay) {
                case "Monday":
                    monday += className + "\n";
                    break;
                case "Tuesday":
                    tuesday += className + "\n";
                    break;
                case "Wednesday":
                    wednesday += className + "\n";
                    break;
                case "Thursday":
                    thursday += className + "\n";
                    break;
                case "Friday":
                    friday += className + "\n";
                    break;
            }
        }

        return new WeeklySchedule(monday, tuesday, wednesday, thursday, friday);
    }

    private List<Course> getStudentClasses(String name) {
        List<Course> studentCourses = new ArrayList<>();
        for (Course course : courses) {
            if (course.getStudents().contains(name)) {
                studentCourses.add(course);
            }
        }
        return studentCourses;
    }

    private List<Course> getClassroomClasses(String classroomName) {
        List<Course> classroomCourses = new ArrayList<>();
        List<String> courseNames = matching.get(classroomName);

        if (courseNames != null) {
            for (String courseName : courseNames) {
                Course course = getCourseByName(courseName);
                if (course != null) {
                    classroomCourses.add(course);
                }
            }
        }

        return classroomCourses;
    }

    private Course getCourseByName(String courseName) {
        for (Course course : courses) {
            if (course.getCourseName().equals(courseName)) {
                return course;
            }
        }
        return null;  // Return null if the course is not found
    }
    private WeeklySchedule createClassroomSchedule(String classroomName) {
        // Retrieve the list of courses assigned to the given classroom
        List<Course> classroomCourses = getClassroomClasses(classroomName);

        // Initialize strings for each day of the week
        String monday = "", tuesday = "", wednesday = "", thursday = "", friday = "";

        // Loop through the courses assigned to the classroom and assign them to the correct day
        for (Course classObj : classroomCourses) {
            String classDay = classObj.getDay();  // Get the day for the course (e.g., "Monday", "Tuesday", etc.)
            String className = classObj.getCourseName();  // Get the name of the course (e.g., "Math 101")

            // Assign the class to the appropriate day
            switch (classDay) {
                case "Monday":
                    monday += className + "\n";  // Use '\n' to separate multiple courses on the same day
                    break;
                case "Tuesday":
                    tuesday += className + "\n";
                    break;
                case "Wednesday":
                    wednesday += className + "\n";
                    break;
                case "Thursday":
                    thursday += className + "\n";
                    break;
                case "Friday":
                    friday += className + "\n";
                    break;
                default:
                    break;
            }
        }

        // Create and return the WeeklySchedule object
        return new WeeklySchedule(monday, tuesday, wednesday, thursday, friday);
    }

    public void refreshSchedules() {

    }

    public void downloadSchedule() {
        System.out.println("Downloading schedule...");
        // Logic to download schedule
    }

    public void addCourse() {
        System.out.println("Adding new course...");
        // Logic to add a course
    }

    public void addStudent() {
        System.out.println("Adding new student...");
        // Logic to add a student
    }

    public void goBack() {
        System.out.println("Going back...");
        // Logic to navigate back
    }
}
