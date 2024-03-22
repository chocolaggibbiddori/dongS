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
            // TODO [2024-03-22]: 인스턴스 만들 때 미리 세팅 해버리기?
            Yaml.readSettings(configPath);
            // TODO [2024-03-22]: getScheduleList 메서드 사용? CSVReader는 내부 클래스로 감추고
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
