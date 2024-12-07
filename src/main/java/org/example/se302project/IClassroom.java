package org.example.se302project;

import java.util.List;

public interface IClassroom {
    String getClassroomName();
    void setClassroomName(String name);
    int getCapacity();
    void setCapacity(int capacity);
    List<String> getAvailableTimes();
    void setAvailableTimes(List<String> availableTimes);
}
