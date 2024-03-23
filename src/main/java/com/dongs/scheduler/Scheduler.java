package com.dongs.scheduler;

import com.dongs.common.exception.InvalidExtensionException;

import java.io.FileNotFoundException;

public interface Scheduler {

    void readAndInspect(String filePath) throws FileNotFoundException, InvalidExtensionException;

    static Scheduler getInstance() {
        return DefaultScheduler.getInstance();
    }
}
