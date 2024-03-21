package com.dongs;

import com.dongs.scheduler.CSVReader;
import com.dongs.scheduler.Schedule;
import com.dongs.scheduler.Scheduler;
import lombok.extern.java.Log;

import java.io.FileNotFoundException;
import java.util.List;

@Log
public class Main {

    public static void main(String[] args) {
        String filePath = "src/main/resources/data.csv";

        try {
            List<Schedule> scheduleList = CSVReader.readSchedulesFromCSV(filePath);
            Scheduler scheduler = Scheduler.getInstance();
            scheduler.add(scheduleList);
        } catch (FileNotFoundException e) {
            log.info("There is no file");
        }

        System.out.println("Finish!");
    }
}
