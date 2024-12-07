package org.example.se302project;

import java.util.List;

public class Classroom implements IClassroom {
    private String classroomName;
    private int capacity;
    private List<String> availableTimes;

    public Classroom(String classroomName, int capacity, List<String> availableTimes) {
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
}
