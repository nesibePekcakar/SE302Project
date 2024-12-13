package org.example.se302project;

public class Course {
    private String courseName;
    private String startTime;
    private int duration;
    private String lecturer;
    private int capacity;

    public Course(String courseName, String startTime, int duration, String lecturer, int capacity) {
        this.courseName = courseName;
        this.startTime = startTime;
        this.duration = duration;
        this.lecturer = lecturer;
        this.capacity = capacity;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return courseName + " by " + lecturer + " at " + startTime + " (" + duration + " mins, capacity: " + capacity + ")";
    }
}
