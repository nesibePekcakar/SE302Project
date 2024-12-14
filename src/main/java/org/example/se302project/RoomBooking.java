package org.example.se302project;

import java.util.*;
import java.util.stream.Collectors;

public class RoomBooking {
    private List<Classroom> classrooms; // List of all available classrooms
    private List<Course> scheduledCourses; // List of all scheduled courses

    // Constructor
    public RoomBooking(List<Classroom> classrooms, List<Course> scheduledCourses) {
        this.classrooms = classrooms;
        this.scheduledCourses = scheduledCourses;
    }

    // Method to automatically assign the best room to a course and return a map
    public HashMap<String, String> assignRoomToClass(Course courseObj) {
        // Create a hash map to store the course-classroom assignments
        HashMap<String, String> classRoomAssignments = new HashMap<>();

        // Get a list of classrooms sorted by increasing capacity
        List<Classroom> sortedClassrooms = classrooms.stream()
                .sorted(Comparator.comparingInt(Classroom::getCapacity))
                .collect(Collectors.toList());

        boolean assigned = false;

        for (Classroom classroom : sortedClassrooms) {
            if (isRoomSuitable(courseObj, classroom)) {
                courseObj.setClassroom(classroom); // Assign the room to the course
                classRoomAssignments.put(courseObj.getCourseName(), classroom.getClassroomName()); // Add to hash map
                System.out.println("Assigned " + classroom.getClassroomName() + " to course " + courseObj.getCourseName());
                assigned = true;
                break; // No need to check further once a suitable classroom is found
            }
        }

        if (!assigned) {
            classRoomAssignments.put(courseObj.getCourseName(), "No suitable classroom available"); // If no assignment found
            System.out.println("No suitable classroom available for " + courseObj.getCourseName());
        }

        return classRoomAssignments;  // Return the map
    }

    // Method to automatically assign rooms to all courses and return a map of assignments
    public HashMap<String, String> assignRoomsToAllClasses() {
        // Create a hash map to store the overall assignments
        HashMap<String, String> allAssignments = new HashMap<>();

        // Iterate over each scheduled course and try to assign a room
        for (Course courseObj : scheduledCourses) {
            HashMap<String, String> assignment = assignRoomToClass(courseObj);  // Get the assignment for each course

            // Add the assignment to the overall map
            allAssignments.putAll(assignment);
        }

        return allAssignments;  // Return the final map of assignments
    }

    // Helper method to check if a room is suitable
    private boolean isRoomSuitable(Course courseObj, Classroom classroom) {
        // Check if the classroom has enough capacity
        if (classroom.getCapacity() < courseObj.getStudents().size()) {
            return false;
        }

        // Check if the classroom is available during the course's schedule
        for (Course scheduledCourse : scheduledCourses) {
            if (scheduledCourse.getClassroom() == classroom) {
                for (String time : courseObj.getSchedule().getScheduledTimes()) {
                    if (scheduledCourse.getSchedule().getScheduledTimes().contains(time)) {
                        return false; // Schedule conflict
                    }
                }
            }
        }

        return true; // Room is suitable
    }

    // Method to change the assigned classroom for a course
    public boolean changeClassroom(Course courseObj, Classroom newClassroom) {
        if (isRoomSuitable(courseObj, newClassroom)) {
            courseObj.setClassroom(newClassroom);
            System.out.println("Classroom for " + courseObj.getCourseName() + " changed to " + newClassroom.getClassroomName());
            return true;
        } else {
            System.out.println("New classroom " + newClassroom.getClassroomName() + " is not suitable for " + courseObj.getCourseName());
            return false;
        }
    }

    // Method to remove a classroom assignment for a course
    public void removeClassroomAssignment(Course courseObj) {
        Classroom previousClassroom = courseObj.getClassroom();
        if (previousClassroom != null) {
            courseObj.setClassroom(null);
            System.out.println("Removed classroom assignment for " + courseObj.getCourseName());
        } else {
            System.out.println(courseObj.getCourseName() + " has no assigned classroom to remove.");
        }
    }

    // Method to reassign classrooms for all courses
    public void reassignRooms() {
        // Clear all assignments
        for (Course courseObj : scheduledCourses) {
            courseObj.setClassroom(null);
        }

        // Reassign rooms using the best possible assignment
        assignRoomsToAllClasses();
    }

    // Method to list all current assignments
    public void listAssignments() {
        for (Course courseObj : scheduledCourses) {
            Classroom assignedClassroom = courseObj.getClassroom();
            if (assignedClassroom != null) {
                System.out.println("Course: " + courseObj.getCourseName() + " -> Room: " + assignedClassroom.getClassroomName());
            } else {
                System.out.println("Course: " + courseObj.getCourseName() + " has no assigned room.");
            }
        }
    }
}
