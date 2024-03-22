package com.dongs;

import com.dongs.scheduler.Scheduler;
import com.dongs.settings.InvalidExtensionException;
import com.dongs.settings.Yaml;
import lombok.extern.java.Log;

import java.io.FileNotFoundException;

@Log
public class Main {

    public static void main(String[] args) {
        String configPath = "src/main/resources/config.yml";

        try {
            String filePath = "src/main/resources/data.csv";
            Scheduler scheduler = Scheduler.getInstance();
            scheduler.readAndInspect(filePath);
        } catch (FileNotFoundException e) {
            log.info("There is no file");
        } catch (InvalidExtensionException e) {
            log.info(e.getMessage());
        }

        System.out.println("Finish!");
    }
}
