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
            // TODO [2024-03-22]: 인스턴스 만들 때 미리 세팅 해버리기?
            Yaml.readSettings(configPath);

            Scheduler scheduler = Scheduler.getInstance();
            scheduler.readAndInspect();
        } catch (FileNotFoundException e) {
            log.info("There is no file");
        } catch (InvalidExtensionException e) {
            log.info(e.getMessage());
        }

        System.out.println("Finish!");
    }
}
