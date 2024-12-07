package org.example.se302project;

class Course {
    String courseName;
    String startTime;
    int duration;
    String lecturer;
    int capacity;

    public Course(String courseName, String startTime, int duration, String lecturer, int capacity) {
        this.courseName = courseName;
        this.startTime = startTime;
        this.duration = duration;
        this.lecturer = lecturer;
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return courseName + " by " + lecturer + " at " + startTime + " (" + duration + " mins, capacity: " + capacity + ")";
    }
}

