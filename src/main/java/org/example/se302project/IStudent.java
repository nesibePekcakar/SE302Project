package org.example.se302project;

import java.util.List;

public interface IStudent {
    String getName();
    void setName(String name);
    List<Course> getClasses();
    void enrollInClass(Course classObj);
    void dropClass(Course classObj);
}
