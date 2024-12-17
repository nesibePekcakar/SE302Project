package org.example.se302project;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.List;

public class ScheduleViewController {

    @FXML
    private ListView<String> studentListView;
    @FXML
    private ListView<String> classroomListView;

    private Schedule studentSchedule= new Schedule();
    private Schedule classroomSchedule= new Schedule();
    CourseManager cm = new CourseManager();
    private List<Classroom> classrooms= cm.ReadClassrooms(cm.getClassroomCapacityFilePath());
    private List<Course> courses= cm.ReadCourses(cm.getCoursesFilePath());

    @FXML
    public void initialize() {
        setSchedule(studentSchedule,classroomSchedule);
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
    private void addCourseToEmptySlot(Schedule studentSchedule, Course course) {
        String courseDay = course.getDay();
        String courseTime = course.getStartTime();
        String courseSlot = courseDay + " " + courseTime;

        for (String studentName : course.getStudents()) {
            List<String> scheduledTimes = studentSchedule.getScheduledTimes();

            if (scheduledTimes.contains(courseSlot)) {
                System.out.println("The slot " + courseSlot + " is already occupied for student " + studentName);
            } else {
                studentSchedule.addStudentSchedule(studentName, course.getCourseName(), courseSlot);
                System.out.println("Added course '" + course.getCourseName() + "' for " + studentName + " at " + courseSlot);
            }
        }
    }

}
