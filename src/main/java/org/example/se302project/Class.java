package org.example.se302project;

import java.util.ArrayList;
import java.util.List;

public class Class implements IClass {
    private String className;
    private IClassroom classroom;
    private ISchedule schedule;
    private List<IStudent> students;

    public Class(String className) {
        this.className = className;
        this.students = new ArrayList<>();
    }

    @Override
    public void setClassroom(IClassroom classroom) {
        this.classroom = classroom;
    }

    @Override
    public IClassroom getClassroom() {
        return this.classroom;
    }

    @Override
    public void setSchedule(ISchedule schedule) {
        this.schedule = schedule;
    }

    @Override
    public ISchedule getSchedule() {
        return this.schedule;
    }

    @Override
    public void addStudent(IStudent student) {
        this.students.add(student);
    }

    @Override
    public void removeStudent(IStudent student) {
        this.students.remove(student);
    }

    @Override
    public List<IStudent> getStudents() {
        return this.students;
    }
}
