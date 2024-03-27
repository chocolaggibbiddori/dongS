package com.dongs;

import com.dongs.common.Paths;
import com.dongs.common.exception.InvalidExtensionException;
import com.dongs.scheduler.Scheduler;
import com.dongs.settings.Setting;
import com.dongs.settings.Settings;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;

@Slf4j
public class Main {

    public static void main(String[] args) {
        Setting setting = Settings.readSettingFromConfigYaml();
        String dataFilename = setting.data().filename().value();
        String filePath = Paths.getResourcesPathInMain("", dataFilename, ".csv");
        Scheduler scheduler = Scheduler.getInstance();

        try {
            scheduler.readAndInspect(filePath);
            log.info("Finish!");
        } catch (InvalidExtensionException | FileNotFoundException e) {
            log.info("There is no data file");
        }
    }
}
