package org.example.se302project;

import java.util.List;

public interface ISchedule {
    List<String> getScheduledTimes();
    void setScheduledTimes(List<String> times);
}

