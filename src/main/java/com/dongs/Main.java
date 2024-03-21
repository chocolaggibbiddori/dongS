package com.dongs;

import com.dongs.scheduler.CSVReader;
import com.dongs.scheduler.Schedule;
import com.dongs.scheduler.Scheduler;
import com.dongs.settings.InvalidExtensionException;
import com.dongs.settings.Yaml;
import lombok.extern.java.Log;

import java.io.FileNotFoundException;
import java.util.List;

@Log
public class Main {

    public static void main(String[] args) {
        String configPath = "src/main/resources/config.yml";
        String dataPath = "src/main/resources/data.csv";

        try {
            Yaml.readSettings(configPath);
            List<Schedule> scheduleList = CSVReader.readSchedulesFromCSV(dataPath);
            Scheduler scheduler = Scheduler.getInstance();
            scheduler.add(scheduleList);
        } catch (FileNotFoundException e) {
            log.info("There is no file");
        } catch (InvalidExtensionException e) {
            log.info(e.getMessage());
        }

        System.out.println("Finish!");
    }
}
