package com.dongs.scheduler;

import java.io.FileNotFoundException;

public interface Scheduler {

    void readAndInspect() throws FileNotFoundException;

    static Scheduler getInstance() {
        return DefaultScheduler.getInstance();
    }
}
