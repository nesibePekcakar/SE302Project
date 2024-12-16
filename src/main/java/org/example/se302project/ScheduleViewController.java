package org.example.se302project;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.List;

public class ScheduleViewController {

    @FXML
    private ListView<String> studentListView;
    @FXML
    private ListView<String> classroomListView;

    private Schedule studentSchedule;
    private Schedule classroomSchedule;

    public void setSchedule(Schedule studentSchedule, Schedule classroomSchedule) {
        this.studentSchedule = studentSchedule;
        this.classroomSchedule = classroomSchedule;

        // Populate the ListViews with the schedules
        populateScheduleListView();
    }

    private void populateScheduleListView() {
        // Populate the student schedule list view
        List<String> studentScheduledTimes = studentSchedule.getScheduledTimes();
        studentListView.getItems().addAll(studentScheduledTimes);

        // Populate the classroom schedule list view
        List<String> classroomScheduledTimes = classroomSchedule.getScheduledTimes();
        classroomListView.getItems().addAll(classroomScheduledTimes);
    }
}
