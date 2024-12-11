package org.example.se302project;

import java.util.List;

public interface ISchedule {
    List<String> getScheduledTimes();
    void setScheduledTimes(List<String> times);

    // New methods
    boolean conflictsWith(ISchedule otherSchedule);
    void addScheduledTime(String time);
    void removeScheduledTime(String time);
}
