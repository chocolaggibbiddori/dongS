package com.dongs;

import com.dongs.common.exception.InvalidExtensionException;
import com.dongs.scheduler.Scheduler;
import com.dongs.settings.Setting;
import lombok.extern.java.Log;

import java.io.FileNotFoundException;

@Log
public class Main {

    public static void main(String[] args) {
        Setting setting = Setting.getInstance();
        String dataFilename = setting.data().filename().value();
        String filePath = "src/main/resources/%s.csv".formatted(dataFilename);
        Scheduler scheduler = Scheduler.getInstance();

        try {
            scheduler.readAndInspect(filePath);
            System.out.println("Finish!");
        } catch (InvalidExtensionException | FileNotFoundException e) {
            log.info("There is no data file");
        }
    }
}
