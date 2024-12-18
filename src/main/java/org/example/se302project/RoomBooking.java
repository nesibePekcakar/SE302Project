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
    public HashMap<String, List<String>> assignRoomToClass(Course courseObj) {
        // Create a hash map to store the course-classroom assignments (multiple courses per classroom)
        HashMap<String, List<String>> classRoomAssignments = new HashMap<>();

        // Get a list of classrooms sorted by increasing capacity
        List<Classroom> sortedClassrooms = classrooms.stream()
                .sorted(Comparator.comparingInt(Classroom::getCapacity))
                .collect(Collectors.toList());

        boolean assigned = false;

        for (Classroom classroom : sortedClassrooms) {
            if (isRoomSuitable(courseObj, classroom)) {
                courseObj.setClassroom(classroom); // Assign the room to the course

                // Add the course to the classroom's list of assignments
                classRoomAssignments.computeIfAbsent(classroom.getClassroomName(), k -> new ArrayList<>())
                        .add(courseObj.getCourseName());

                System.out.println("Assigned " + classroom.getClassroomName() + " to course " + courseObj.getCourseName());
                assigned = true;
                break; // No need to check further once a suitable classroom is found
            }
        }

        if (!assigned) {
            System.out.println("No suitable classroom available for " + courseObj.getCourseName());
        }

        return classRoomAssignments;  // Return the map with multiple course assignments per classroom
    }


    // Method to automatically assign rooms to all courses and return a map of assignments
    public HashMap<String, List<String>> assignRoomsToAllClasses() {
        // Create a hash map to store the overall assignments
        HashMap<String, List<String>> allAssignments = new HashMap<>();

        // Iterate over each scheduled course and try to assign a room
        for (Course courseObj : scheduledCourses) {
            HashMap<String, List<String>> assignment = assignRoomToClass(courseObj);  // Get the assignment for each course

            // Merge the current course assignments with the overall map
            for (Map.Entry<String, List<String>> entry : assignment.entrySet()) {
                allAssignments.merge(entry.getKey(), entry.getValue(), (existingList, newList) -> {
                    existingList.addAll(newList);
                    return existingList;
                });
            }
        }

        return allAssignments;  // Return the final map of assignments with multiple courses per room
    }


    // Helper method to check if a room is suitable
    private boolean isRoomSuitable(Course courseObj, Classroom classroom) {
        // Check if the classroom has enough capacity
        if (classroom.getCapacity() < courseObj.getStudents().size()) {
            return false;
        }

        // Check for any time conflicts for this room
        for (Course scheduledCourse : scheduledCourses) {
            if (scheduledCourse.getClassroom() == classroom && scheduledCourse.getDay().equals(courseObj.getDay())) {
                // Check if there is a time overlap with the scheduled course
                if (isTimeConflict(scheduledCourse, courseObj)) {
                    return false;  // Conflict detected
                }
            }
        }

        return true;  // Room is suitable
    }

    private boolean isTimeConflict(Course course1, Course course2) {
        // Convert the start times to minutes since midnight
        int startTime1 = convertTimeToMinutes(course1.getStartTime());
        int startTime2 = convertTimeToMinutes(course2.getStartTime());

        // Get the duration of each course in minutes (1 lecture hour = 45 minutes)
        int duration1 = course1.getDurationInLectureHours() * 45; // Convert lecture hours to minutes
        int duration2 = course2.getDurationInLectureHours() * 45; // Convert lecture hours to minutes

        // Calculate the end time for each course
        int endTime1 = startTime1 + duration1;
        int endTime2 = startTime2 + duration2;

        // Check if there is a time conflict between the two courses
        return !(endTime1 <= startTime2 || endTime2 <= startTime1);
    }


    private int convertTimeToMinutes(String time) {
        // Helper method to convert time to minutes since midnight
        String[] timeParts = time.split(":");
        int hours = Integer.parseInt(timeParts[0]);
        int minutes = Integer.parseInt(timeParts[1].substring(0, 2));
        String ampm = timeParts[1].substring(2);

        if (ampm.equals("PM") && hours < 12) {
            hours += 12;
        } else if (ampm.equals("AM") && hours == 12) {
            hours = 0;
        }

        return hours * 60 + minutes;
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
        // Get all assignments (assuming it's a HashMap<String, List<String>>)
        HashMap<String, List<String>> allAssignments = assignRoomsToAllClasses();  // This retrieves the current room assignments

        // Iterate over each classroom and its list of assigned courses
        for (Map.Entry<String, List<String>> entry : allAssignments.entrySet()) {
            String classroomName = entry.getKey();
            List<String> assignedCourses = entry.getValue();

            // Print the classroom and the list of courses assigned to it
            System.out.println("Room: " + classroomName + " -> Courses: " + String.join(", ", assignedCourses));
        }
    }

}
