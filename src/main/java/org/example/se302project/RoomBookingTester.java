package org.example.se302project;

import java.util.Arrays;
import java.util.List;

public class RoomBookingTester {
    public static void main(String[] args) {
        // Step 1: Create classrooms
        Classroom room1 = new Classroom("Room A", 30);
        Classroom room2 = new Classroom("Room B", 50);
        Classroom room3 = new Classroom("Room C", 20);

        // Step 2: Create schedules
        Schedule schedule1 = new Schedule();
        schedule1.setScheduledTimes(Arrays.asList("9:00-10:00"));

        Schedule schedule2 = new Schedule();
        schedule2.setScheduledTimes(Arrays.asList("10:00-11:00"));

        Schedule schedule3 = new Schedule();
        schedule3.setScheduledTimes(Arrays.asList("11:00-12:00"));

        // Step 3: Create classes
        Class class1 = new Class("Math 101");
        class1.setSchedule(schedule1);

        Class class2 = new Class("Physics 201");
        class2.setSchedule(schedule2);

        Class class3 = new Class("History 301");
        class3.setSchedule(schedule3);

        // Add students to classes
        for (int i = 0; i < 25; i++) {
            class1.addStudent(new Student("Student" + i));
        }

        for (int i = 0; i < 40; i++) {
            class2.addStudent(new Student("Student" + (i + 25)));
        }

        for (int i = 0; i < 15; i++) {
            class3.addStudent(new Student("Student" + (i + 65)));
        }

        // Step 4: Create RoomBooking instance
        List<IClassroom> classrooms = Arrays.asList(room1, room2, room3);
        List<IClass> scheduledClasses = Arrays.asList(class1, class2, class3);
        RoomBooking roomBooking = new RoomBooking(classrooms, scheduledClasses);

        // Step 5: Test automatic room assignment
        System.out.println("===== Automatic Room Assignment =====");
        roomBooking.assignRoomsToAllClasses();

        // Step 6: List current assignments
        System.out.println("\n===== Current Assignments =====");
        roomBooking.listAssignments();

        // Step 7: Test manual reassignment
        System.out.println("\n===== Manual Reassignment =====");
        System.out.println("Attempting to reassign Physics 201 to Room C...");
        roomBooking.changeClassroom(class2, room3); // Should fail due to capacity
        System.out.println("Attempting to reassign History 301 to Room A...");
        roomBooking.changeClassroom(class3, room1); // Should succeed

        // Step 8: List assignments after reassignment
        System.out.println("\n===== Assignments After Reassignment =====");
        roomBooking.listAssignments();

        // Step 9: Test clearing and reassigning rooms
        System.out.println("\n===== Reassigning All Rooms =====");
        roomBooking.reassignRooms();

        // Step 10: List assignments after reassigning
        System.out.println("\n===== Assignments After Reassigning =====");
        roomBooking.listAssignments();

        // Step 11: Test removing room assignments
        System.out.println("\n===== Removing Room Assignments =====");
        roomBooking.removeClassroomAssignment(class1);
        roomBooking.listAssignments();
    }
}
