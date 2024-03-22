package com.dongs;

import com.dongs.scheduler.Scheduler;
import lombok.extern.java.Log;

import java.io.FileNotFoundException;

@Log
public class Main {

    public static void main(String[] args) {
        try {
            String filePath = "src/main/resources/data.csv";
            Scheduler scheduler = Scheduler.getInstance();
            scheduler.readAndInspect(filePath);
        } catch (FileNotFoundException e) {
            log.info("There is no data file");
        }

        System.out.println("Finish!");
    }
}
