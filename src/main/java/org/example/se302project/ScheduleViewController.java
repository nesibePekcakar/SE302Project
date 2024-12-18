package org.example.se302project;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.io.FileWriter;
import java.io.IOException;
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
    @FXML
    private TextField studentNameField; // TextField to input student name

    private Map<String, List<String>> roomAssignments;

    private List<String> students = new ArrayList<>();

    private CourseManager cm = new CourseManager();
    private List<Classroom> classrooms = cm.ReadClassrooms(cm.getClassroomCapacityFilePath());
    private List<Course> courses = cm.ReadCourses(cm.getCoursesFilePath());
    private Map<String, List<String>> matching = cm.readMatching(cm.getMatchingFilePath());
    private String selectedCourseName = "";
    String push= "";

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

        setupColumnClickHandler(mondayColumn, "Monday");
        setupColumnClickHandler(tuesdayColumn, "Tuesday");
        setupColumnClickHandler(wednesdayColumn, "Wednesday");
        setupColumnClickHandler(thursdayColumn, "Thursday");
        setupColumnClickHandler(fridayColumn, "Friday");
    }
    private void setupColumnClickHandler(TableColumn<WeeklySchedule, String> column, String day) {
        column.setCellFactory(tc -> {
            TableCell<WeeklySchedule, String> cell = new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(item); // Set the text of the cell
                    if (item != null && !empty) {
                        setOnMouseClicked(event -> {
                            selectedCourseName = item;
                            System.out.println("Clicked on " + item + " on " + day);
                            openLessonDetails(item, day);
                        });
                    } else {
                        setOnMouseClicked(null); // Remove click event for empty cells
                    }
                }
            };
            return cell;
        });
    }
    private void openLessonDetails(String lessonName, String day) {
        if (lessonName == null || lessonName.isEmpty()) return;

        // You can load a new FXML or display lesson details in a dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Lesson Details");
        alert.setHeaderText("Details for " + lessonName);
        alert.setContentText("Day: " + day + "\nLesson: " + lessonName);
        alert.showAndWait();
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

    /*private WeeklySchedule CreateStudentSchedule(String studentName) {
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
    }*/

    private WeeklySchedule CreateStudentSchedule(String studentName) {

        List<String> timeSlots = Arrays.asList(
                "08:30", "09:25", "10:20", "11:15", "12:10",
                "13:05", "14:00", "14:55", "15:50"
        );


        Map<String, String[]> dailySchedules = new HashMap<>();
        dailySchedules.put("Monday", new String[timeSlots.size()]);
        dailySchedules.put("Tuesday", new String[timeSlots.size()]);
        dailySchedules.put("Wednesday", new String[timeSlots.size()]);
        dailySchedules.put("Thursday", new String[timeSlots.size()]);
        dailySchedules.put("Friday", new String[timeSlots.size()]);


        List<Course> studentClasses = getStudentClasses(studentName);



        for (Course course : studentClasses) {
            String classDay = course.getDay();
            String classTime = course.getStartTime();
            String className = course.getCourseName();
            int duration = course.getDurationInLectureHours();


            String[] scheduleForDay = dailySchedules.get(classDay);
            if (scheduleForDay != null) {
                int timeIndex = timeSlots.indexOf(classTime);
                if (timeIndex >= 0) {
                    for (int i = 0; i < duration; i++) {
                        int currentIndex = timeIndex + i;
                        if (currentIndex < timeSlots.size()) {
                            scheduleForDay[currentIndex] = className;
                        }
                    }

                }
            }
        }


        String monday = formatDaySchedule(dailySchedules.get("Monday"), timeSlots);
        String tuesday = formatDaySchedule(dailySchedules.get("Tuesday"), timeSlots);
        String wednesday = formatDaySchedule(dailySchedules.get("Wednesday"), timeSlots);
        String thursday = formatDaySchedule(dailySchedules.get("Thursday"), timeSlots);
        String friday = formatDaySchedule(dailySchedules.get("Friday"), timeSlots);


        return new WeeklySchedule(monday, tuesday, wednesday, thursday, friday);
    }

    private String formatDaySchedule(String[] scheduleForDay, List<String> timeSlots) {
        StringBuilder formattedSchedule = new StringBuilder();

        for (int i = 0; i < timeSlots.size(); i++) {
            String time = timeSlots.get(i);
            String courseName = scheduleForDay[i];
            if (courseName != null) {
                formattedSchedule.append(time).append(" - ").append(courseName).append("\n");
            } else {
                formattedSchedule.append(time).append(" - \n");
            }
        }

        return formattedSchedule.toString();
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
            int duration = classObj.getDurationInLectureHours();
            String startTime= classObj.getStartTime();
            List<String> courseSlots = new ArrayList<>();
            String currentStartTime = startTime;

            for (int i = 0; i < duration; i++) {

                currentStartTime = calculateNextAvailableTime(currentStartTime, 1); // Bir sonraki saati hesapla


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
        }

        // Create and return the WeeklySchedule object
        return new WeeklySchedule(monday, tuesday, wednesday, thursday, friday);
    }



//    public void downloadSchedule() {
//        System.out.println("Downloading schedule...");
//        // Logic to download schedule
//    }


    public void addCourse() {
        System.out.println("Adding new course...");
        String selectedStudent = studentsChoiceBox.getValue();

        if (selectedStudent == null) {
            System.out.println("Please select a student first.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Course");
        dialog.setHeaderText("Enter Course Information");
        dialog.setContentText("Enter course name: , day: , start time: , duration: , lecturer name:");

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


        if (!isValidStartTime(courseTime)) {
            System.out.println("Invalid start time. Only specific start times are allowed.");
            return;
        }

        for (Course course : courses) {
            if (course.getStudents().contains(selectedStudent)) {
                if (isTimeConflict(course.getDay(), course.getStartTime(), course.getDurationInLectureHours(), courseDay, courseTime, courseDuration)) {
                    System.out.println("The slot is already occupied for student " + selectedStudent);
                    return;
                }
            }
        }

        List<String> courseSlots = new ArrayList<>();
        String currentStartTime = courseTime;

        for (int i = 0; i < courseDuration; i++) {
            courseSlots.add(courseDay + " " + currentStartTime);
            currentStartTime = calculateNextAvailableTime(currentStartTime, 1); // Bir sonraki saati hesapla
        }


        for (String slot : courseSlots) {
            Course newCourse = new Course(courseName, courseDay, slot.split(" ")[1], 1, lecturer, 1, new ArrayList<>(List.of(selectedStudent)));


            Classroom availableClassroom = findAvailableClassroom(slot.split(" ")[1]);
            if (availableClassroom != null) {
                newCourse.setClassroom(availableClassroom);
                System.out.println("Assigned classroom: " + availableClassroom.getClassroomName());
            } else {
                System.out.println("No available classroom. Course added without a classroom.");
            }

            courses.add(newCourse);
            System.out.println("Added course: " + courseName + " for student " + selectedStudent + " at " + slot);
        }

        populateScheduleTable(selectedStudent);
    }
    private boolean isValidStartTime(String time) {
        String timePattern = "^([01]?[0-9]|2[0-3]):([0-5][0-9])$";

        if (time == null || !time.matches(timePattern)) {
            return false;
        }
        return true;
    }


    private String calculateNextAvailableTime(String startTime, int duration) {
        int startTimeInMinutes = convertTimeToMinutes(startTime);

        int nextStartTimeInMinutes = startTimeInMinutes + (duration * 55); // 45 dakika ders + 10 dakika ara

        int hours = nextStartTimeInMinutes / 60;
        int minutes = nextStartTimeInMinutes % 60;

        return String.format("%02d:%02d", hours, minutes);
    }

    private Classroom findAvailableClassroom(String courseSlot) {
        for (Classroom classroom : classrooms) {
            boolean classroomConflict = courses.stream()
                    .filter(course -> course.getClassroom() != null && course.getClassroom().equals(classroom))
                    .anyMatch(course -> (course.getDay() + " " + course.getStartTime()).equals(courseSlot));

            if (!classroomConflict) {
                return classroom;
            }
        }
        return null;
    }

    private boolean isTimeConflict(String existingDay, String existingStartTime, int existingDuration, String newDay, String newStartTime, int newDuration) {
        // Gün farklı ise çakışma yok
        if (!existingDay.equals(newDay)) {
            return false;
        }

        // Saatleri dakikaya çevir
        int existingStart = convertTimeToMinutes(existingStartTime);
        int existingEnd = existingStart + (existingDuration * 45);

        int newStart = convertTimeToMinutes(newStartTime);
        int newEnd = newStart + (newDuration * 45);

        // Zaman dilimlerinde çakışma olup olmadığını kontrol et
        return newStart < existingEnd && newEnd > existingStart;
    }

    private int convertTimeToMinutes(String time) {
        String[] timeParts = time.split(":");
        int hours = Integer.parseInt(timeParts[0]);
        int minutes = Integer.parseInt(timeParts[1]);
        return hours * 60 + minutes;
    }


    public void addStudent() {

        System.out.println("Adding new student...");
        System.out.println("Adding new student...");
        String studentName = studentNameField.getText().trim();
        ArrayList<String> selectedCourseStudents = getCourseByName(selectedCourseName).getStudents();

        if (!studentName.isEmpty() && !selectedCourseStudents.contains(studentName)) {
            getCourseByName(selectedCourseName).getStudents().add(studentName);
            studentsChoiceBox.getItems().add(studentName); // Update the ChoiceBox
            System.out.println(getCourseByName(selectedCourseName).getStudents());
            studentNameField.clear(); // Clear the input field
            showAlert("Success", "Student added successfully: " + studentName);
        } else {
            showAlert("Error", "Student name is either empty or already exists.");
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // You can use other alert types like WARNING or ERROR
        alert.setTitle(title);
        alert.setHeaderText(null); // Optional: Remove the header
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void goBack() {
        System.out.println("Going back...");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("matching-view.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) scheduleTableView.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                showProblem("Error", "An error occurred while loading the previous screen: " + e.getMessage());
            }
    }

    private void showProblem(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showScheduleHelp() {
        Alert helpAlert = new Alert(AlertType.INFORMATION);
        helpAlert.setTitle("View Schedule - Help");
        helpAlert.setHeaderText("How to View a Student's Schedule");
        Label label = new Label("1. Select a student from the dropdown menu.\n"
                + "2. The schedule table will automatically update.\n"
                + "3. You may add a non-conflicting course to a student via the related button.");
        label.setWrapText(true);

        helpAlert.getDialogPane().setContent(label);

        helpAlert.getDialogPane().setPrefSize(500, 400);
        helpAlert.showAndWait();
    }

    @FXML
    private void downloadSchedule() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Schedule Data");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialFileName("schedule.csv");

        File file = fileChooser.showSaveDialog(scheduleTableView.getScene().getWindow());
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("Monday,Tuesday,Wednesday,Thursday,Friday\n");

                for (Object item : scheduleTableView.getItems()) {
                    if (item instanceof Classroom classroom) {
                        writer.write(String.format("%s,%d,%s\n",
                                classroom.getClassroomName(),
                                classroom.getCapacity(),
                                "Assigned courses info here"
                        ));
                    }
                }

                showAlert("Download Successful", "The schedule data has been saved to: " + file.getAbsolutePath());
            } catch (IOException e) {
                showAlert("Error", "An error occurred while saving the file: " + e.getMessage());
            }
        }
    }

}
