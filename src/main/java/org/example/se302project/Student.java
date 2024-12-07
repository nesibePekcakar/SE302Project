package org.example.se302project;

import java.util.ArrayList;
import java.util.List;

public class Student implements IStudent {
    private String name;
    private List<IClass> classes;

    public Student(String name) {
        this.name = name;
        this.classes = new ArrayList<>();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<IClass> getClasses() {
        return this.classes;
    }

    @Override
    public void enrollInClass(IClass classObj) {
        this.classes.add(classObj);
    }

    @Override
    public void dropClass(IClass classObj) {
        this.classes.remove(classObj);
    }
}
