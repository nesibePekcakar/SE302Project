package org.example.se302project;

import java.util.List;

public interface IStudent {
    String getName();
    void setName(String name);
    List<IClass> getClasses();
    void enrollInClass(IClass classObj);
    void dropClass(IClass classObj);
}
