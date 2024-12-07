package org.example.se302project;

import java.util.List;

public interface IClass {
    void setClassroom(IClassroom classroom);
    IClassroom getClassroom();
    void setSchedule(ISchedule schedule);
    ISchedule getSchedule();
    void addStudent(IStudent student);
    void removeStudent(IStudent student);
    List<IStudent> getStudents();
}

