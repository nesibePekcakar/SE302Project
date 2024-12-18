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
            String selectedStudent = studentsChoiceBox.getValue();
            String selectedClassroom = classroomsChoiceBox.getValue();

            if (selectedStudent == null || selectedClassroom == null) {
                System.out.println("Please select a student and a classroom first.");
                return; // Eğer seçim yapılmamışsa işlemi durdur
            }


            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Add Course");
            dialog.setHeaderText("Enter Course Information");
            dialog.setContentText("Enter course name: , day: , start time: , duration: ,lecturer name:");

            Optional<String> result = dialog.showAndWait();
            if (result.isEmpty()) {
                System.out.println("Course creation cancelled.");
                return;
            }


            String[] courseDetails = result.get().split(",");
            if (courseDetails.length < 4) {
                System.out.println("Invalid input format. Expected: courseName, day, startTime, duration, lecturer");
                return;
            }

            String courseName = courseDetails[0].trim();
            String courseDay = courseDetails[1].trim();
            String courseTime = courseDetails[2].trim();
            int courseDuration;
            try {
                courseDuration = Integer.parseInt(courseDetails[3].trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid duration format. Please enter a valid number.");
                return;
            }
            String lecturer = courseDetails.length > 4 ? courseDetails[4].trim() : "Unknown Lecturer";


            String courseSlot = courseDay + " " + courseTime;


            boolean studentConflict = courses.stream()
                    .filter(course -> course.getStudents().contains(selectedStudent))
                    .anyMatch(course -> (course.getDay() + " " + course.getStartTime()).equals(courseSlot));

            if (studentConflict) {
                System.out.println("The slot " + courseSlot + " is already occupied for student " + selectedStudent);
                return;
            }


            boolean classroomConflict = courses.stream()
                    .filter(course -> course.getClassroom() != null && course.getClassroom().getClassroomName().equals(selectedClassroom))
                    .anyMatch(course -> (course.getDay() + " " + course.getStartTime()).equals(courseSlot));

            if (classroomConflict) {
                System.out.println("The slot " + courseSlot + " is already occupied for classroom " + selectedClassroom);
                return;
            }


            Course newCourse = new Course(courseName, courseDay, courseTime, courseDuration, lecturer, 1, new ArrayList<>(List.of(selectedStudent)));
            newCourse.setClassroom(getClassroomByName(selectedClassroom));
            courses.add(newCourse);

            System.out.println("Added course: " + courseName + " for student " + selectedStudent + " in classroom " + selectedClassroom);

            populateScheduleTable(selectedStudent);
        }

        private Classroom getClassroomByName(String classroomName) {
            for (Classroom classroom : classrooms) {
                if (classroom.getClassroomName().equals(classroomName)) {
                    return classroom;
                }
            }
            return null; // Eğer sınıf bulunamazsa null döndür
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
