package org.example.se302project;

import java.util.ArrayList;
import java.util.List;

public class Schedule implements ISchedule {
    private List<String> scheduledTimes;

    public Schedule() {
        this.scheduledTimes = new ArrayList<>();
    }

    @Override
    public List<String> getScheduledTimes() {
        return new ArrayList<>(this.scheduledTimes);
    }

    @Override
    public void setScheduledTimes(List<String> times) {
        this.scheduledTimes = new ArrayList<>(times);
    }

    @Override
    public boolean conflictsWith(ISchedule otherSchedule) {
        for (String time : this.scheduledTimes) {
            if (otherSchedule.getScheduledTimes().contains(time)) {
                return true; // Conflict found
            }
        }
        return false; // No conflicts
    }

    @Override
    public void addScheduledTime(String time) {
        if (!this.scheduledTimes.contains(time)) {
            this.scheduledTimes.add(time);
        }
    }

    @Override
    public void removeScheduledTime(String time) {
        this.scheduledTimes.remove(time);
    }

    // Add a method to add scheduled time for both students and classrooms
    public void addStudentSchedule(String studentName, String time) {
        // Add the schedule for the student
        this.scheduledTimes.add(studentName + ": " + time);
    }

    public void addClassroomSchedule(String classroomName, String time) {
        // Add the schedule for the classroom
        this.scheduledTimes.add(classroomName + ": " + time);
    }
}
