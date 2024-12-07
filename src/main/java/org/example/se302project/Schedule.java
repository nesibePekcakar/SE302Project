package org.example.se302project;

import java.util.List;

public class Schedule implements ISchedule {
    private List<String> scheduledTimes;

    public Schedule(List<String> scheduledTimes) {
        this.scheduledTimes = scheduledTimes;
    }

    @Override
    public List<String> getScheduledTimes() {
        return this.scheduledTimes;
    }

    @Override
    public void setScheduledTimes(List<String> times) {
        this.scheduledTimes = times;
    }
}
