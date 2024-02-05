package com.chocola.scheduler;

import java.util.Collection;

public interface Scheduler {

    void add(Collection<Schedule> schedules);

    static Scheduler getInstance() {
        return DefaultScheduler.getInstance();
    }
}
