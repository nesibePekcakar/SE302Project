package org.example.se302project;

import java.util.ArrayList;
import java.util.List;

public class Classroom implements IClassroom {
    private String classroomName;
    private int capacity;
    private List<String> availableTimes;
    private ISchedule schedule; // Schedule for the classroom

    public Classroom(String classroomName, int capacity,List<String> availableTimes) {
        this.classroomName = classroomName;
        this.capacity = capacity;
        this.availableTimes = availableTimes;
    }

    @Override
    public String getClassroomName() {
        return this.classroomName;
    }

    @Override
    public void setClassroomName(String name) {
        this.classroomName = name;
    }

    @Override
    public int getCapacity() {
        return this.capacity;
    }

    @Override
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public List<String> getAvailableTimes() {
        return this.availableTimes;
    }

    @Override
    public void setAvailableTimes(List<String> availableTimes) {
        this.availableTimes = availableTimes;
    }

    // New methods

    @Override
    public ISchedule getSchedule() {
        return this.schedule;
    }

    @Override
    public void setSchedule(ISchedule schedule) {
        this.schedule = schedule;
    }

    @Override
    public boolean isAvailableFor(ISchedule schedule) {
        if (this.schedule == null) {
            return true; // No schedule assigned, classroom is available.
        }
        return !this.schedule.conflictsWith(schedule);
    }
}
