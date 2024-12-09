package org.example.se302project;

import java.util.ArrayList;
import java.util.List;

public interface IClass {
    String getClassName();
    void setClassroom(IClassroom classroom);
    IClassroom getClassroom();
    void setSchedule(ISchedule schedule);
    ISchedule getSchedule();
    void addStudent(IStudent student);
    void removeStudent(IStudent student);
    List<IStudent> getStudents();

    // New methods
    int getRequiredCapacity();
    boolean hasConflictingSchedule(IClassroom classroom);
}
