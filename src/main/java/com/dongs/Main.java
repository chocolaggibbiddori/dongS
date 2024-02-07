package com.dongs;

import com.dongs.scheduler.CSVReader;
import com.dongs.scheduler.Schedule;
import com.dongs.scheduler.Scheduler;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        String filePath = "src/main/resources/data.csv";

        List<Schedule> scheduleList = CSVReader.readSchedulesFromCSV(filePath);
        Scheduler scheduler = Scheduler.getInstance();
        scheduler.add(scheduleList);

        System.out.println("Finish!");
    }
}
