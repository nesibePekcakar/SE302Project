package org.example.se302project;

import java.util.ArrayList;

public class Course {
    private String courseName;
    private String day;  // If you want to keep the day separately
    private String startTime;  // Keeping it as String, assuming format like "HH:mm"
    private int durationInLectureHours;  // Duration in lecture hours
    private String lecturer;
    private int attendance;  // Renamed from capacity to students, assuming it reflects enrolled students
    private ArrayList<String> students;
    public Course(String courseName, String day, String startTime, int durationInLectureHours, String lecturer, int attendance, ArrayList<String> students) {
        this.courseName = courseName;
        this.day = day;
        this.startTime = startTime;
        this.durationInLectureHours = durationInLectureHours;
        this.lecturer = lecturer;
        this.attendance = attendance;
        this.students=students;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getDurationInLectureHours() {
        return durationInLectureHours;
    }

    public void setDurationInLectureHours(int durationInLectureHours) {
        this.durationInLectureHours = durationInLectureHours;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    @Override
    public String toString() {
        return courseName + " by " + lecturer + " on " + day + " at " + startTime + " (" + durationInLectureHours + " hours, " + attendance + " students)";
    }
}
