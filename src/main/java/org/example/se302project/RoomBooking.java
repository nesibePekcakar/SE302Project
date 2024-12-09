package org.example.se302project;
import java.util.*;
import java.util.stream.Collectors;

public class RoomBooking {
    private List<IClassroom> classrooms; // List of all available classrooms
    private List<IClass> scheduledClasses; // List of all scheduled classes

    // Constructor
    public RoomBooking(List<IClassroom> classrooms, List<IClass> scheduledClasses) {
        this.classrooms = classrooms;
        this.scheduledClasses = scheduledClasses;
    }

    // Method to automatically assign the best room to a class
    public boolean assignRoomToClass(IClass classObj) {
        // Get a list of classrooms sorted by increasing capacity
        List<IClassroom> sortedClassrooms = classrooms.stream()
                .sorted(Comparator.comparingInt(IClassroom::getCapacity))
                .collect(Collectors.toList());

        for (IClassroom classroom : sortedClassrooms) {
            if (isRoomSuitable(classObj, classroom)) {
                classObj.setClassroom(classroom);
                System.out.println("Assigned " + classroom.getClassroomName() + " to class " + classObj.getClassName());
                return true;
            }
        }

        System.out.println("No suitable classroom available for " + classObj.getClassName());
        return false;
    }

    // Helper method to check if a room is suitable
    private boolean isRoomSuitable(IClass classObj, IClassroom classroom) {
        // Check if the classroom has enough capacity
        if (classroom.getCapacity() < classObj.getStudents().size()) {
            return false;
        }

        // Check if the classroom is available during the class's schedule
        for (IClass scheduledClass : scheduledClasses) {
            if (scheduledClass.getClassroom() == classroom) {
                for (String time : classObj.getSchedule().getScheduledTimes()) {
                    if (scheduledClass.getSchedule().getScheduledTimes().contains(time)) {
                        return false; // Schedule conflict
                    }
                }
            }
        }

        return true; // Room is suitable
    }

    // Method to automatically assign rooms to all classes
    public void assignRoomsToAllClasses() {
        for (IClass classObj : scheduledClasses) {
            if (!assignRoomToClass(classObj)) {
                System.out.println("Failed to assign a room for class " + classObj.getClassName());
            }
        }
    }

    // Method to change the assigned classroom for a class
    public boolean changeClassroom(IClass classObj, IClassroom newClassroom) {
        if (isRoomSuitable(classObj, newClassroom)) {
            classObj.setClassroom(newClassroom);
            System.out.println("Classroom for " + classObj.getClassName() + " changed to " + newClassroom.getClassroomName());
            return true;
        } else {
            System.out.println("New classroom " + newClassroom.getClassroomName() + " is not suitable for " + classObj.getClassName());
            return false;
        }
    }

    // Method to remove a classroom assignment for a class
    public void removeClassroomAssignment(IClass classObj) {
        IClassroom previousClassroom = classObj.getClassroom();
        if (previousClassroom != null) {
            classObj.setClassroom(null);
            System.out.println("Removed classroom assignment for " + classObj.getClassName());
        } else {
            System.out.println(classObj.getClassName() + " has no assigned classroom to remove.");
        }
    }

    // Method to reassign classrooms for all classes
    public void reassignRooms() {
        // Clear all assignments
        for (IClass classObj : scheduledClasses) {
            classObj.setClassroom(null);
        }

        // Reassign rooms using the best possible assignment
        assignRoomsToAllClasses();
    }

    // Method to list all current assignments
    public void listAssignments() {
        for (IClass classObj : scheduledClasses) {
            IClassroom assignedClassroom = classObj.getClassroom();
            if (assignedClassroom != null) {
                System.out.println("Class: " + classObj.getClassName() + " -> Room: " + assignedClassroom.getClassroomName());
            } else {
                System.out.println("Class: " + classObj.getClassName() + " has no assigned room.");
            }
        }
    }
}
