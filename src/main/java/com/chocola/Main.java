package com.chocola;

import com.chocola.scheduler.CSVReader;
import com.chocola.scheduler.Schedule;
import com.chocola.scheduler.Scheduler;

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
