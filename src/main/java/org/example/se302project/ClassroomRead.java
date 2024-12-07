package org.example.se302project;

class ClassroomRead {
    String classroomName;
    int capacity;

    public ClassroomRead(String classroomName, int capacity) {
        this.classroomName = classroomName;
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return classroomName + " with capacity " + capacity;
    }
}
