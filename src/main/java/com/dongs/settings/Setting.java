package com.dongs.settings;

import com.dongs.common.exception.InvalidExtensionException;
import lombok.extern.java.Log;

import java.io.FileNotFoundException;

@Log
public class Setting {

    private static final Setting INSTANCE = new Setting();

    private final Schedule schedule = Schedule.getInstance();

    private Setting() {
        String configPath = "src/main/resources/config.yml";

        try {
            Yaml.readSettings(configPath);
        } catch (InvalidExtensionException | FileNotFoundException e) {
            log.info("There is no configuration file");
        }
    }

    public static Setting getInstance() {
        return INSTANCE;
    }

    public Schedule schedule() {
        return schedule;
    }
}
