package org.example.se302project;

import java.util.ArrayList;
import java.util.List;

public class Schedule implements ISchedule {
    private List<String> scheduledTimes;

    public Schedule() {
        this.scheduledTimes = new ArrayList<>();
    }

    @Override
    public List<String> getScheduledTimes() {
        return new ArrayList<>(this.scheduledTimes);
    }

    @Override
    public void setScheduledTimes(List<String> times) {
        this.scheduledTimes = new ArrayList<>(times);
    }

    @Override
    public boolean conflictsWith(ISchedule otherSchedule) {
        for (String time : this.scheduledTimes) {
            if (otherSchedule.getScheduledTimes().contains(time)) {
                return true; // Conflict found
            }
        }
        return false; // No conflicts
    }

    @Override
    public void addScheduledTime(String time) {
        if (!this.scheduledTimes.contains(time)) {
            this.scheduledTimes.add(time);
        }
    }

    @Override
    public void removeScheduledTime(String time) {
        this.scheduledTimes.remove(time);
    }
}
